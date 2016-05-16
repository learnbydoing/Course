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
                using (ECTDBContext entities = new ECTDBContext())
                {

                    var category = from c in entities.Categories
                                  where c.CategoryId == id
                                  select c;
                    model.EditableCategory = category.FirstOrDefault();
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

        public ActionResult Update(Category pCategory)
        {
            OperationViewModel model = new OperationViewModel();
            try
            {
                using (ECTDBContext entities = new ECTDBContext())
                {

                    Category original = entities.Categories.Find(pCategory.CategoryId);
                    entities.Entry(original).CurrentValues.SetValues(pCategory);
                    entities.SaveChanges();
                }
                model.Message = "Category Updated.";
                model.IsError = false;
                model.Id = pCategory.CategoryId;
            }

            catch (Exception e)
            {
                model.Message = "Category not updated. Error:" + e.Message;
                model.IsError = true;
                model.Id = pCategory.CategoryId;
            }
            return View(model);
        }

    }
}