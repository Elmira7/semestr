package servlets.admin;

import entities.Category;
import entities.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.ProductService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/product")
public class AdminProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = (ProductService) getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Category, List<Category>> categoryListMap = productService.findAllCategories();
        Map<String, List<String>> categoryString = productService.mapFromCategoryToString(categoryListMap);
        List<Product> products = productService.listProduct();
        HttpSession session = req.getSession();

        if (session.getAttribute("errorMessage") != null){
            req.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        if(req.getParameter("update") != null && req.getParameter("update").equals("product")){
            if (req.getParameter("product") != null) {
                Product product = productService.findProduct(Long.parseLong(req.getParameter("product")));
                req.setAttribute("categoryMap", categoryListMap);
                req.setAttribute("categoryString", categoryString);
                req.setAttribute("countFeature", 1);
                req.setAttribute("product", product);
                req.getRequestDispatcher("/WEB-INF/jsp/admin-product-edit.jsp").forward(req,resp);
            } else {
                //TODO ошибка ввода данных
            }
        } else {

            req.setAttribute("categoryMap", categoryListMap);
            req.setAttribute("categoryString", categoryString);
            req.setAttribute("productList", products);
            req.getRequestDispatcher("/WEB-INF/jsp/admin-product.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

            Map<Category, List<Category>> categoryListMap = productService.findAllCategories();

            if (req.getParameter("add") != null && req.getParameter("add").equals("category")) {

                if (req.getParameter("name") != null
                        && !req.getParameter("name").equals("")
                        && req.getParameter("parent_name") != null){

                    if (req.getParameter("parent_name").equals("")){

                        if (categoryListMap.keySet().contains(Category.builder().name(req.getParameter("name")).build())){
                            session.setAttribute("errorMessage", "Категория уже существует");
                            resp.sendRedirect("/admin/product");
                            return;
                        } else {
                            productService.addCategory(Category.builder()
                                    .name(req.getParameter("name"))
                                    .build());
                        }

                    } else {
                        if (categoryListMap.keySet().contains(Category.builder().name(req.getParameter("parent_name")).build())){
                            List<Category> listCategory = categoryListMap.get(Category.builder().name(req.getParameter("parent_name")).build());
                            Category category = Category.builder()
                                    .name(req.getParameter("name"))
                                    .parentName(req.getParameter("parent_name"))
                                    .build();
                            if (listCategory.contains(category)){
                                session.setAttribute("errorMessage", "Категория уже существует");
                                resp.sendRedirect("/admin/product");
                                return;
                            } else {
                                productService.addCategory(category);
                            }
                        } else {
                            session.setAttribute("errorMessage", "Родительской категории не существует");
                            resp.sendRedirect("/admin/product");
                            return;
                        }

                    }

                    resp.sendRedirect("/admin/product");

                } else {

                    session.setAttribute("errorMessage", "Ошибка ввода данных");
                    resp.sendRedirect("/admin/product");
                    return;

                }

            } else if (req.getParameter("remove") != null && req.getParameter("remove").equals("category")) {


            } else if (req.getParameter("add") != null && req.getParameter("add").equals("product")){

                Product product = createProduct(session, resp, req);

                if (product != null) {

                    productService.addProduct(product);
                    product = productService.findProduct(product.getName(), product.getCategory());
                    resp.sendRedirect("/save/image?product_id=" + product.getId());

                } else {
                    return;
                }

            } else if (req.getParameter("remove") != null && req.getParameter("remove").equals("product")){

                Product product = null;
                try {
                    product = productService.findProduct(Long.parseLong(req.getParameter("product_id")));
                } catch (NumberFormatException e){
                    session.setAttribute("errorMessage", "Ошибка ввода данных");
                    resp.sendRedirect("/admin/product");
                    return;
                }

                if (product != null){
                    productService.removeProduct(product);
                } else {
                    session.setAttribute("errorMessage", "Такого товара не сущесвует");
                    resp.sendRedirect("/admin/product");
                    return;
                }

                resp.sendRedirect("/admin/product");

            } else if (req.getParameter("update") != null && req.getParameter("update").equals("product")) {

                Product product = createProduct(session, resp, req);

                if (product != null) {

                    productService.updateProduct(product);
                    product = productService.findProduct(product.getName(), product.getCategory());
                    resp.sendRedirect("/save/image?product_id=" + product.getId());

                } else {
                    return;
                }

            }

            else {
                session.setAttribute("errorMessage", "Неизвестный запрос");
                resp.sendRedirect("/admin/product");
                return;
            }
    }

    private Product createProduct(HttpSession session, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        Product product = null;
        if(req.getParameter("add").equals("product")) {
            product = Product.builder().build();
        } else if (req.getParameter("update").equals("product")){
            if (req.getParameter("product_id") != null) {
                try {
                    product = productService.findProduct(Long.parseLong(req.getParameter("product_id")));
                    if(product == null){
                        session.setAttribute("errorMessage", "Товара не существует");
                        resp.sendRedirect("/admin/product");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        Map<String, String> feature = new HashMap<>();
        Map<Category, List<Category>> categoryListMap = productService.findAllCategories();
        Map<String, List<String>> categoryString = productService.mapFromCategoryToString(categoryListMap);

        for(int i = 1; i <= 10; i++){
            if(req.getParameter("feature" + i) != null && !req.getParameter("feature" + i).equals("")
                    && req.getParameter("feature_value" + i) != null && !req.getParameter("feature_value" + i).equals("")){
                feature.put(req.getParameter("feature" + i), req.getParameter("feature_value" + i));
            }
        }
        product.setFeature(feature);

        if (req.getParameter("name") != null && !req.getParameter("name").equals("")){

            product.setName(req.getParameter("name"));

        } else {
            session.setAttribute("errorMessage", "Ошибка ввода имени товара");
            resp.sendRedirect("/admin/product");
            return null;
        }

        if (req.getParameter("description") != null && !req.getParameter("description").equals("")){

            product.setDescription(req.getParameter("description"));

        } else {
            session.setAttribute("errorMessage", "Ошибка ввода описания товара");
            resp.sendRedirect("/admin/product");
            return null;
        }

        if (req.getParameter("price") != null && !req.getParameter("price").equals("")){
            try {
                product.setPrice(Integer.parseInt(req.getParameter("price")));
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Ошибка ввода цены товара");
                resp.sendRedirect("/admin/product");
                return null;
            }

        } else {
            session.setAttribute("errorMessage", "Ошибка ввода цены товара");
            resp.sendRedirect("/admin/product");
            return null;
        }

        if (req.getParameter("count") != null && !req.getParameter("count").equals("")){
            try {
                product.setCount(Integer.parseInt(req.getParameter("count")));
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Ошибка ввода количества продуктов");
                resp.sendRedirect("/admin/product");
                return null;
            }

        } else {
            session.setAttribute("errorMessage", "Ошибка ввода количества продуктов");
            resp.sendRedirect("/admin/product");
            return null;
        }

        if (req.getParameter("parent_category") != null){

            if (!req.getParameter("parent_category").equals("")){

                if(categoryString.containsKey(req.getParameter("parent_category"))){
                    if(req.getParameter("category") != null){
                        if (req.getParameter("category").equals("")){
                            product.setCategory(Category.builder()
                                    .name(req.getParameter("parent_category"))
                                    .build());
                        } else {

                            if (categoryString.get(req.getParameter("parent_category")).contains(req.getParameter("category"))){
                                product.setCategory(Category.builder()
                                        .name(req.getParameter("category"))
                                        .parentName(req.getParameter("parent_category"))
                                        .build());
                            } else {
                                session.setAttribute("errorMessage", "У товара отстутствует подкатегория");
                                resp.sendRedirect("/admin/product");
                                return null;
                            }

                        }
                    } else {
                        session.setAttribute("errorMessage", "Ошибка ввода данных");
                        resp.sendRedirect("/admin/product");
                        return null;
                    }
                } else {
                    session.setAttribute("errorMessage", "Ошибка ввода данных");
                    resp.sendRedirect("/admin/product");
                    return null;
                }
            }

        } else {
            session.setAttribute("errorMessage", "Ошибка ввода данных");
            resp.sendRedirect("/admin/product");
            return null;
        }

        if (product.getCategory() == null) {
            session.setAttribute("errorMessage", "Не указана категория");
            resp.sendRedirect("/admin/product");
            return null;
        }
        return product;
    }

}
