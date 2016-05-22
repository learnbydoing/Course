using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
using GameStore.WebUI.Models.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
{
    public class ConsoleController : Controller
    {
        // GET: Console
        public ActionResult Index()
        {
            List<ProductDTO> list = null;
            if (System.Web.HttpContext.Current.Cache["ProductList1"] != null)
            {
                list = (List<ProductDTO>)System.Web.HttpContext.Current.Cache["ProductList1"];
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    var query = from product in context.Products
                                where product.CategoryId == 1
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                                select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                    list = query.ToList();
                    System.Web.HttpContext.Current.Cache["ProductList1"] = list;
                }
            }

            return View(list);
        }
    }
}