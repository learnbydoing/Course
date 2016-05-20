using GameStore.WebUI.Models;
using GameStore.WebUI.Models.DTO;
using GameStore.Domain;
using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
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
                if (System.Web.HttpContext.Current.Cache["Category" + id] != null)
                {
                    CategoryDTO categoryDTO = (CategoryDTO)System.Web.HttpContext.Current.Cache["Category" + id];
                    model.CategoryId = categoryDTO.CategoryId;
                    model.CategoryName = categoryDTO.CategoryName;
                }
                else
                {
                    using (GameStoreDBContext context = new GameStoreDBContext())
                    {
                        var category = from c in context.Categories
                                       where c.CategoryId == id
                                       select c;
                        var cat = category.FirstOrDefault();
                        model.CategoryId = cat.CategoryId;
                        model.CategoryName = cat.CategoryName;
                        CategoryDTO categoryDTO = new CategoryDTO { CategoryId = cat.CategoryId, CategoryName = cat.CategoryName };
                        System.Web.HttpContext.Current.Cache["Category" + id] = categoryDTO;
                    }
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
                using (GameStoreDBContext context = new GameStoreDBContext())
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
                        System.Web.HttpContext.Current.Cache.Remove("CategoryList");
                        System.Web.HttpContext.Current.Cache.Remove("Category"+ value.CategoryId);
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