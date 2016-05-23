using GameStore.WebUI.Models;
using GameStore.WebUI.Models.DTO;
using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
{
    public class ProductController : Controller
    {
        // GET: Product
        public ActionResult Index()
        {
            List<Category> list = null;
            if (System.Web.HttpContext.Current.Cache["CategoryList"] != null)
            {
                list = (List<Category>)System.Web.HttpContext.Current.Cache["CategoryList"];
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    list = context.Categories.ToList();
                    System.Web.HttpContext.Current.Cache["CategoryList"] = list;
                }
            }

            ViewBag.Categories = list;
            List<Category> alllist = new List<Category>(list);
            alllist.Insert(0, new Category { CategoryId = 0, CategoryName = "Select All" });
            ViewBag.CategoryFilter = alllist;
            return View();
        }
        public ActionResult Console()
        {
            List<ProductDTO> list = GetProductsByCategory(1);
            ViewBag.Title = "Console";
            return View("List", list);
        }
        public ActionResult Accessory()
        {
            List<ProductDTO> list = GetProductsByCategory(2);
            ViewBag.Title = "Accessory";
            return View("List", list);
        }
        public ActionResult Game()
        {
            List<ProductDTO> list = GetProductsByCategory(3);
            ViewBag.Title = "Game";
            return View("List", list);
        }

        private List<ProductDTO> GetProductsByCategory(int categoryid)
        {
            List<ProductDTO> list = new List<ProductDTO>();
            if (System.Web.HttpContext.Current.Cache["ProductList" + categoryid] != null)
            {
                list = (List<ProductDTO>)System.Web.HttpContext.Current.Cache["ProductList" + categoryid];
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    var query = from product in context.Products
                                where product.CategoryId == categoryid
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                                select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                    list = query.ToList();
                    System.Web.HttpContext.Current.Cache["ProductList" + categoryid] = list;
                }
            }

            return list;
        }

        public ActionResult Search(string productname)
        {
            List<ProductDTO> list = new List<ProductDTO>();

            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                var query = from product in context.Products
                            where product.ProductName.ToLower().Contains(productname.ToLower())
                            join category in context.Categories
                              on product.CategoryId equals category.CategoryId
                            select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                list = query.ToList();
            }
            ViewBag.Title = "Search";
            return View("List", list);
        }

        public ActionResult Detail(int id)
        {
            ProductDTO model = null;
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                var query = from product in context.Products
                            where product.ProductId == id
                            join category in context.Categories
                              on product.CategoryId equals category.CategoryId
                            select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                model = query.FirstOrDefault();
            }
            return View(model);
        }
    }
}