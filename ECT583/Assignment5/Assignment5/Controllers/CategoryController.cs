using Assignment5.Models;
using ECTDBDal;
using ECTDBDal.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Assignment5.Controllers
{
    public class CategoryController : Controller
    {
        // GET: Category
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Edit(int id)
        {
            CategoryEditViewModel model = new CategoryEditViewModel();
            model.IsErrorStatusMessage = false;
            try
            {

                model.Title = "Edit Category";
                using (ECTDBContext context = new ECTDBContext())
                {

                    var category = from c in context.Categories
                                  where c.CategoryId == id
                                  select c;
                    var cat = category.FirstOrDefault();
                    model.CategoryId = cat.CategoryId;
                    model.CategoryName = cat.CategoryName;
                }
                return View(model);
            }
            catch (Exception e)
            {
                model.IsErrorStatusMessage = true;
                model.StatusMessage = "Error: " + e.Message;
                return View(model);
            }
        }

        public ActionResult Update(Category value)
        {
            OperationViewModel model = new OperationViewModel();
            try
            {               
                using (ECTDBContext context = new ECTDBContext())
                {
                    bool exist = context.Categories.Where(c => c.CategoryId != value.CategoryId).Any(c => c.CategoryName.Equals(value.CategoryName, StringComparison.OrdinalIgnoreCase));
                    if (exist)
                    {
                        model.Message = "Category [" + value.CategoryName + "] is already existed, please try another name!";
                        model.IsError = true;
                        model.Id = value.CategoryId;
                    }
                    else
                    {
                        Category original = context.Categories.Find(value.CategoryId);
                        context.Entry(original).CurrentValues.SetValues(value);
                        context.SaveChanges();
                        model.Message = "Category Updated.";
                        model.IsError = false;
                        model.Id = value.CategoryId;
                    }                    
                }                
            }
            catch (Exception e)
            {
                model.Message = "Category not updated. Error:" + e.Message;
                model.IsError = true;
                model.Id = value.CategoryId;
            }
            return View(model);
        }
    }
}