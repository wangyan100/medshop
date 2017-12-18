package com.hellokoding.account.web;

import com.hellokoding.account.model.Product;
import com.hellokoding.account.model.ShoppingCart;
import com.hellokoding.account.model.StatisticReport;
import com.hellokoding.account.model.Tourguide;
import com.hellokoding.account.model.User;
import com.hellokoding.account.service.*;
import com.hellokoding.account.validator.UserValidator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private FileService fileService;

    @Autowired
    private MedShopService medShopService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private MailService mailService;

    @Autowired
    private String mailTo;

    private static final String MAIL_SUBJECT = "medshop order";

    private static final String MAIL_CONTENT = "Please find the order in the attachment.";

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout, HttpSession session) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            session.invalidate();
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model, HttpSession session, HttpServletRequest request) {
        logger.debug("###session### : " + session);
        java.util.List<Product> products = medShopService.getAllProducts();
        model.addAttribute("products", products);

        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setOrderCreator(request.getUserPrincipal().getName());
            session.setAttribute("shoppingCart", shoppingCart);
        }

        String action = request.getParameter("action");
        String pzn = request.getParameter("pzn");

        if (action != null && action.contains("addShoppingCart") && pzn != null) {
            String amountStr = request.getParameter("amount");
            logger.info(amountStr);
            //default value 1
            int amount = 1;
            if (amountStr != null) {
                amount = Integer.parseInt(amountStr);
            }
            medShopService.addProductToShoppingCart((ShoppingCart) session.getAttribute("shoppingCart"), pzn, amount);
            logger.debug("Orders: " + shoppingCart.getOrders());
            //logger.debug("shoppingCart orders"+((ShoppingCart)session.getAttribute("shoppingCart").);
        }

        return "welcome";
    }

    @RequestMapping(value = {"/upload"}, method = RequestMethod.GET)
    public String upload(Model model) {
        logger.debug("upload is called ");
        return "upload";
    }

    @RequestMapping(value = {"/shoppingcart"}, method = RequestMethod.GET)
    public String shoppingcart(Model model, HttpServletRequest request, HttpSession session) {

        String tourGuideName = request.getParameter("tourGuideName");
        String tourGuideID = request.getParameter("tourGuideID");
        String touristName = request.getParameter("touristName");
        String shopName = request.getParameter("shopName");
        String pickupDate = request.getParameter("pickupDate");
        String pickupTime = request.getParameter("pickupTime");

        model.addAttribute("tourGuideName", tourGuideName);
        model.addAttribute("tourGuideID", tourGuideID);
        model.addAttribute("touristName", touristName);
        model.addAttribute("shopName", shopName);
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("pickupTime", pickupTime);

        String action = request.getParameter("action");
        String pzn = request.getParameter("pzn");

        if (action != null && action.equalsIgnoreCase("plus") && pzn != null) {
            medShopService.changeProductNumberAtShoppingCart((ShoppingCart) session.getAttribute("shoppingCart"), pzn, 1);
        }

        if (action != null && action.equalsIgnoreCase("minus") && pzn != null) {
            medShopService.changeProductNumberAtShoppingCart((ShoppingCart) session.getAttribute("shoppingCart"), pzn, -1);

        }

        return "shoppingcart";
    }

    @RequestMapping(value = {"/shoppingcart"}, method = RequestMethod.POST)
    public String shoppingcartOrder(
            @RequestParam("pickupDate") String pickupDate,
            @RequestParam("pickupTime") String pickupTime,
            @RequestParam("tourGuideName") String tourGuideName,
            @RequestParam("tourGuideID") String tourGuideID,
            @RequestParam("touristName") String touristName,
            @RequestParam("shopName") String shopName,
            Model model, HttpServletRequest request, HttpSession session) throws Exception {

        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        shoppingCart.setPickupDate(formatter.parse(pickupDate));
        shoppingCart.setPickupTime(pickupTime);
        shoppingCart.setTourGuideName(tourGuideName);
        shoppingCart.setTourGuideID(tourGuideID);
        shoppingCart.setTouristName(touristName);
        shoppingCart.setShopName(shopName);
        medShopService.makeOrder(shoppingCart);

        // clear shoppingcart and redirect to orderview
        session.removeAttribute("shoppingCart");

        return "redirect:/orders";
    }

    @RequestMapping(value = {"/uploadProcess"}, method = RequestMethod.POST)
    public String uploadProcess(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (!file.isEmpty() && file.getOriginalFilename().toLowerCase().contains(".xls")) {
            logger.debug("fileoriginalname###: " + file.getOriginalFilename());
            boolean isSucceed = fileService.loadFile2Database(file.getInputStream());
            if (isSucceed) {
                return "redirect:/welcome";
            }
        }
        return "upload";
    }

    @RequestMapping(value = {"/uploadtourguideProcess"}, method = RequestMethod.POST)
    public String uploadtourguideProcess(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (!file.isEmpty() && file.getOriginalFilename().toLowerCase().contains(".xls")) {
            logger.debug("fileoriginalname###: " + file.getOriginalFilename());
            fileService.loadTourguideFile2Database(file.getInputStream());

        }
        return "upload";
    }

    @RequestMapping(value = {"/orders"}, method = RequestMethod.GET)
    public String orders(
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpSession session) throws Exception {

        model.addAttribute("orders", medShopService.getAllOrders());
        return "orderlist";
    }

    @RequestMapping(value = {"/querytourguide"}, method = RequestMethod.GET)
    @ResponseBody
    public Tourguide querytourguide(@RequestParam("tourguideid") String tourguideid) {
        
        return medShopService.getTourguideByTourguideid(tourguideid);
        
    }

    @RequestMapping(value = {"/report"}, method = RequestMethod.POST)
    public String reportGenerate(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("report") String report,
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpSession session) throws Exception {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date from = formatter.parse(fromDate);
        java.util.Date to = formatter.parse(toDate);
        ArrayList<StatisticReport> statisticReports = null;

        if (report.toLowerCase().contains("shop") && report.toLowerCase().contains("price")) {

            String sql = "select shopname, sum(totalprice) from productorder where "
                    + "status='VALID' and orderdate between :fromDate and :toDate "
                    + "group by shopname order by sum(totalprice) desc ";
            java.util.List<Object[]> list = medShopService.queryReport(sql, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()));

            statisticReports = new ArrayList<StatisticReport>();

            for (Object[] objs : list) {
                int i = 0;
                StatisticReport staticReport = new StatisticReport();
                statisticReports.add(staticReport);
                for (Object obj : objs) {
                    i++;
                    if (i == 1) {
                        staticReport.setShopname(obj.toString());
                        logger.debug(" report shopname:" + staticReport.getShopname());

                    }
                    if (i == 2) {
                        staticReport.setTotalprice((BigDecimal) obj);

                        logger.debug(" report totalprice:" + staticReport.getTotalprice());
                    }
                }

                model.addAttribute("headercolum1", fromDate + " bis " + toDate);
                model.addAttribute("headercolum2", "Apotheke");
                model.addAttribute("headercolum3", "Umsatz €");

                model.addAttribute("statisticReports", statisticReports);
            }

        }

        if (report.toLowerCase().contains("shop") && report.toLowerCase().contains("order")) {

            String sql = "select shopname, count(id) from productorder where "
                    + "status='VALID' and orderdate between :fromDate and :toDate "
                    + "group by shopname order by count(id) desc ";
            java.util.List<Object[]> list = medShopService.queryReport(sql, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()));

            statisticReports = new ArrayList<StatisticReport>();

            for (Object[] objs : list) {
                int i = 0;
                StatisticReport staticReport = new StatisticReport();
                statisticReports.add(staticReport);
                for (Object obj : objs) {
                    i++;
                    if (i == 1) {
                        staticReport.setShopname(obj.toString());
                        logger.debug(" report shopname:" + staticReport.getShopname());

                    }
                    if (i == 2) {
                        staticReport.setAmount(Integer.parseInt(obj.toString()));
                        //staticReport.setTotalprice((BigDecimal) obj);

                        logger.debug(" report totalprice:" + staticReport.getTotalprice());
                    }
                }

                model.addAttribute("headercolum1", fromDate + " bis " + toDate);
                model.addAttribute("headercolum2", "Apotheke");
                model.addAttribute("headercolum3", "Menge");

                model.addAttribute("statisticReports", statisticReports);
            }

        }

        if (report.toLowerCase().contains("tourguide") && report.toLowerCase().contains("price")) {

            String sql = "select tourguidename, sum(totalprice) from productorder where "
                    + "status='VALID' and orderdate between :fromDate and :toDate "
                    + "group by tourguidename order by sum(totalprice) desc ";
            java.util.List<Object[]> list = medShopService.queryReport(sql, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()));

            statisticReports = new ArrayList<StatisticReport>();

            for (Object[] objs : list) {
                int i = 0;
                StatisticReport staticReport = new StatisticReport();
                statisticReports.add(staticReport);
                for (Object obj : objs) {
                    i++;
                    if (i == 1) {
                        staticReport.setTourguidename(obj.toString());
                        //staticReport.setShopname(obj.toString());

                    }
                    if (i == 2) {
                        staticReport.setTotalprice((BigDecimal) obj);

                        logger.debug(" report totalprice:" + staticReport.getTotalprice());
                    }
                }

                model.addAttribute("headercolum1", fromDate + " bis " + toDate);
                model.addAttribute("headercolum2", "ReiseLeiterName");
                model.addAttribute("headercolum3", "Umsatz €");

                model.addAttribute("statisticReports", statisticReports);
            }

        }

        if (report.toLowerCase().contains("tourguide") && report.toLowerCase().contains("order")) {

            String sql = "select tourguidename, count(id) from productorder where "
                    + "status='VALID' and orderdate between :fromDate and :toDate "
                    + "group by tourguidename order by count(id) desc ";
            java.util.List<Object[]> list = medShopService.queryReport(sql, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()));

            statisticReports = new ArrayList<StatisticReport>();

            for (Object[] objs : list) {
                int i = 0;
                StatisticReport staticReport = new StatisticReport();
                statisticReports.add(staticReport);
                for (Object obj : objs) {
                    i++;
                    if (i == 1) {
                        staticReport.setTourguidename(obj.toString());
                        //staticReport.setShopname(obj.toString());

                    }
                    if (i == 2) {
                        staticReport.setAmount(Integer.parseInt(obj.toString()));
                        //staticReport.setTotalprice((BigDecimal) obj);

                        logger.debug(" report totalprice:" + staticReport.getTotalprice());
                    }
                }

                model.addAttribute("headercolum1", fromDate + " bis " + toDate);
                model.addAttribute("headercolum2", "ReiseLeiterName:");
                model.addAttribute("headercolum3", "Menge");

                model.addAttribute("statisticReports", statisticReports);
            }

        }

        return "report";
    }

    @RequestMapping(value = {"/report"}, method = RequestMethod.GET)
    public String report(
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpSession session) throws Exception {

        //model.addAttribute("orders", medShopService.getAllOrders());
        return "report";
    }

    @RequestMapping(value = {"/orderdetail"}, method = RequestMethod.GET)
    public String orderdetail(@RequestParam("id") long id, @RequestParam(value = "action", required = false) String action,
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpSession session) throws Exception {

        if (action != null && action.equalsIgnoreCase("delete")) {

            medShopService.setOrderInvalid(id);
            return "redirect:/orders"; //here match with RequestMapping not modelview
        }

        if (action != null && action.equalsIgnoreCase("addproduct")) {
            String pzn = request.getParameter("pzn");
            String amount = request.getParameter("amount");

            // call service addProudct
            if (pzn != null && amount != null && com.hellokoding.account.util.Util.isInteger(amount)) {
                medShopService.addProductToOrder(id, pzn, Integer.parseInt(amount));
            }
            return "redirect:/orderdetail?id=" + id;
        }

        if (action != null && action.equalsIgnoreCase("removeproduct")) {
            String pzn = request.getParameter("pzn");
            String amount = request.getParameter("amount");

            // call service addProudct
            if (pzn != null && amount != null && com.hellokoding.account.util.Util.isInteger(amount)) {
                medShopService.removeProductFromOrder(id, pzn, Integer.parseInt(amount));
            }
            return "redirect:/orderdetail?id=" + id;
        }

        if (action != null && action.equalsIgnoreCase("removeproduct")) {
            String pzn = request.getParameter("pzn");
            String amount = request.getParameter("amount");

            // call service removeProudct
            if (pzn != null && amount != null) {
            }
            return "redirect:/orderdetail?id" + id;
        }

        model.addAttribute("order", medShopService.getOrder(id));
        return "orderdetail";
    }

    @RequestMapping(value = {"/orderdetaildownload"}, method = RequestMethod.GET)
    public void orderdetaildownload(@RequestParam("id") long id, @RequestParam() String ordernumber,
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + "bestellung" + ordernumber + ".xlsx\"");
        response.setContentType("application/msword");
        XSSFWorkbook document = fileService.createOrderDetail(id);
        document.write(response.getOutputStream());
        //model.addAttribute("order", medShopService.getOrder(id));
        response.flushBuffer();
        // return "orderdetail";
    }

    @RequestMapping(value = {"/orderdetailsendmail"}, method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Response orderdetailsendmail(@RequestParam("id") long id, @RequestParam() String ordernumber,
                                               RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        File tempFile = null;
        OutputStream outputStream = null;
        try {
            tempFile = File.createTempFile("mail-attachment-", ".xlsx");
            outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
            XSSFWorkbook document = fileService.createOrderDetail(id);
            document.write(outputStream);
            outputStream.flush();
            mailService.sendMail(mailTo, MAIL_SUBJECT, MAIL_CONTENT, tempFile.getAbsolutePath());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (tempFile != null) {
                tempFile.delete();
            }
        }
        return Response.ok().build();
    }
}
