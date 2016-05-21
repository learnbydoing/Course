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
            List<CategoryDTO> list = null;
            if (System.Web.HttpContext.Current.Cache["CategoryList"] != null)
            {
                list = (List<CategoryDTO>)System.Web.HttpContext.Current.Cache["CategoryList"];
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    list = context.Categories.Select(s => new CategoryDTO { CategoryId = s.CategoryId, CategoryName = s.CategoryName }).ToList();
                    System.Web.HttpContext.Current.Cache["CategoryList"] = list;
                }
            }

            ViewBag.Categories = list;
            List<CategoryDTO> alllist = new List<CategoryDTO>(list);
            alllist.Insert(0, new CategoryDTO { CategoryId = 0, CategoryName = "Select All" });
            ViewBag.CategoryFilter = alllist;
            return View();
        }

        public ActionResult Edit(int id)
        {            
            ProductEditViewModel model = new ProductEditViewModel();
            model.IsErrorStatusMessage = false;
            try
            {
                model.Title = "Edit Product";
                if (System.Web.HttpContext.Current.Cache["Product" + id] != null)
                {
                    ProductDTO productDTO = (ProductDTO)System.Web.HttpContext.Current.Cache["Product" + id];
                    model.ProductId = productDTO.ProductId;
                    model.ProductName = productDTO.ProductName;
                    model.CategoryId = productDTO.CategoryId;
                    model.Price = productDTO.Price;
                    model.Image = productDTO.Image;
                    model.Condition = productDTO.Condition;
                    model.Discount = productDTO.Discount;
                }
                else
                {
                    using (GameStoreDBContext context = new GameStoreDBContext())
                    {                        
                        var product = from c in context.Products
                                      where c.ProductId == id
                                      select c;
                        Product pro = product.FirstOrDefault();
                        model.ProductId = pro.ProductId;
                        model.ProductName = pro.ProductName;
                        model.CategoryId = pro.CategoryId;
                        model.Price = pro.Price;
                        model.Image = pro.Image;
                        model.Condition = pro.Condition;
                        model.Discount = pro.Discount;
                    }
                }

                List<CategoryDTO> list = null;
                if (System.Web.HttpContext.Current.Cache["CategoryList"] != null)
                {
                    list = (List<CategoryDTO>)System.Web.HttpContext.Current.Cache["CategoryList"];
                }
                else
                {
                    using (GameStoreDBContext context = new GameStoreDBContext())
                    {
                        list = context.Categories.Select(s => new CategoryDTO { CategoryId = s.CategoryId, CategoryName = s.CategoryName }).ToList();
                        System.Web.HttpContext.Current.Cache["CategoryList"] = list;
                    }
                }
                ViewBag.Categories = list;
                return View(model);
            }
            catch (Exception e)
            {
                model.IsErrorStatusMessage = true;
                model.StatusMessage = "Error: " + e.Message;
                return View(model);
            }
        }

        public ActionResult Update(Product value)
        {
            OperationViewModel model = new OperationViewModel();
            try
            {
                if (value.Discount < 0 || value.Discount > 100)
                {
                    model.Message = "Discount must between 0 ~ 100.";
                    model.IsError = true;
                    model.Id = value.ProductId;
                }
                else
                {
                    using (GameStoreDBContext context = new GameStoreDBContext())
                    {
                        bool exist = context.Products.Where(c => c.ProductId != value.ProductId)
                                                     .Any(c => c.ProductName.Equals(value.ProductName, StringComparison.OrdinalIgnoreCase));
                        if (exist)
                        {
                            model.Message = "Product [" + value.ProductName + "] is already existed, please try another name!";
                            model.IsError = true;
                            model.Id = value.ProductId;
                        }
                        else
                        {
                            Product original = context.Products.Find(value.ProductId);
                            System.Web.HttpContext.Current.Cache.Remove("ProductList" + original.CategoryId);
                            context.Entry(original).CurrentValues.SetValues(value);
                            context.SaveChanges();
                            System.Web.HttpContext.Current.Cache.Remove("ProductList");                            
                            System.Web.HttpContext.Current.Cache.Remove("ProductList" + value.CategoryId);
                            System.Web.HttpContext.Current.Cache.Remove("Produc" + original.ProductId);
                            model.Message = "Product Updated!";
                            model.IsError = false;
                            model.Id = value.ProductId;
                        }
                    }
                }                
            }
            catch (Exception e)
            {
                model.Message = "Product not updated. Error:" + e.Message;
                model.IsError = true;
                model.Id = value.ProductId;
            }
            return View(model);
        }
    }
}