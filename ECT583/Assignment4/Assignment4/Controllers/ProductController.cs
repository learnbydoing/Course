using Assignment4.Models;
using ECTDBDal;
using ECTDBDal.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Assignment4.Controllers
{
    public class ProductController : Controller
    {
        // GET: Product
        public ActionResult Index()
        {
            List<Category> list = new List<Category>();
            using (ECTDBContext context = new ECTDBContext())
            {
                list = context.Categories.ToList();
            }            
            ViewBag.Categories = list;
            List<Category> alllist = new List<Category>(list);
            alllist.Insert(0, new Category { CategoryId = 0, CategoryName = "Select All" });
            ViewBag.CategoryFilter = alllist;
            return View();
        }

        public ActionResult Edit(int id)
        {
            List<Category> list = new List<Category>();
            ProductEditViewModel model = new ProductEditViewModel();
            model.IsErrorStatusMessage = false;
            try
            {

                model.Title = "Edit Product";
                using (ECTDBContext context = new ECTDBContext())
                {
                    list = context.Categories.ToList();
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
                    using (ECTDBContext context = new ECTDBContext())
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
                            context.Entry(original).CurrentValues.SetValues(value);
                            context.SaveChanges();
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