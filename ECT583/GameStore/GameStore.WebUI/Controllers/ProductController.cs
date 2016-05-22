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
    }
}