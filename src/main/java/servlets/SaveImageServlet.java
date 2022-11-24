package servlets;

import entities.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.ProductService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "SaveImageServlet", value = "/save/image")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1024,
        maxFileSize = 1024 * 1024 * 1024 *5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class SaveImageServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = (ProductService) getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if(request.getParameter("product_id") != null){
            session.setAttribute("product", request.getParameter("product_id"));
            request.getRequestDispatcher("/WEB-INF/jsp/save-image.jsp").forward(request, response);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String savePath = "C:\\Users\\Elmira\\Desktop\\img";
        String fileName = null;
        String ext = null;

        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }


        for (Part part : request.getParts()) {
            fileName = extractFileName(part);
            fileName = new File(fileName).getName();
            if (fileName.equals("")){
                return;
            }
            ext = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            UUID uuid = UUID.randomUUID();
            fileName = uuid.toString().substring(0, 5) + ext;
            part.write(savePath + File.separator + fileName);
            break;
        }

        if (session.getAttribute("product") != null){

            try {

                Product product = productService.findProduct(Long.parseLong(session.getAttribute("product").toString()));
                product.setPathImage(fileName);
                productService.updateProduct(product);

            } catch (NumberFormatException e){

            }

        }

        response.sendRedirect("/admin/product");

    }

    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";

    }
}
