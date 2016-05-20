using GameStore.WebUI.Models.DTO;
using GameStore.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
{
    [OutputCache(CacheProfile = "StaticUser")]
    public class HomeController : Controller
    {
        // GET: Home
        public ActionResult Index()
        {
            int cagetoryCount = 0;
            if (System.Web.HttpContext.Current.Cache["CategoryList"] != null)
            {
                List<CategoryDTO> list = (List<CategoryDTO>)System.Web.HttpContext.Current.Cache["CategoryList"];
                cagetoryCount = list.Count();
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    List<CategoryDTO> categories = context.Categories.Select(s => new CategoryDTO { CategoryId = s.CategoryId, CategoryName = s.CategoryName }).ToList();
                    System.Web.HttpContext.Current.Cache["CategoryList"] = categories;
                    cagetoryCount = categories.Count();
                }
            }
            ViewBag.CategoryCount = cagetoryCount;

            int productCount = 0;
            if (System.Web.HttpContext.Current.Cache["ProductList"] != null)
            {
                List<ProductDTO> list = (List<ProductDTO>)System.Web.HttpContext.Current.Cache["ProductList"];
                productCount = list.Count();
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    var query = from product in context.Products
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                                select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };

                    List<ProductDTO> products = query.ToList();
                    System.Web.HttpContext.Current.Cache["ProductList"] = products;
                    productCount = products.Count();
                }
            }
            ViewBag.ProductCount = productCount;
            return View();
        }
    }
}