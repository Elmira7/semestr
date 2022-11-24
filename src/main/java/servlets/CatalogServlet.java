package servlets;

import entities.Category;
import entities.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CatalogServlet", value = "/catalog")
public class CatalogServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = (ProductService) getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> productList = new ArrayList<>();
        Category category = null;
        Map<String, List<String>> categoryMap = productService.mapFromCategoryToString(productService.findAllCategories());
        HttpSession session = request.getSession();

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        if (request.getParameter("category") != null) {
            productList = productService.listProductByCategory(request.getParameter("category"));
            category = productService.findCategory(request.getParameter("category"));

            if (category.getParentName() == null){
                List<Category> categories = productService.findAllCategories().get(category);
                for(Category categoryEntity: categories){
                    productList.addAll(productService.listProductByCategory(categoryEntity.getName()));
                }
            }

            request.setAttribute("category", category);
            request.setAttribute("productSet", productList);


        } else {

            request.setAttribute("productSet", productService.listProduct());

        }
        request.setAttribute("categoryMap", categoryMap);
        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
