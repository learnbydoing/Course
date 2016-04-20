using Assignment3.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace Assignment3.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            CustomerListViewModel model = new CustomerListViewModel();
            model.IsError = false;
            try
            {
                model.Title = "Manage Customers";
                using (Assignment3Context context = new Assignment3Context())
                {
                    var customers = from s in context.Customers
                                   orderby s.Id descending
                                   select s;
                    model.Customers = customers.ToList();
                }
                return View(model);
            }
            catch (Exception e)
            {
                model.IsError = true;
                model.Message = "Error: " + e.Message;
                return View(model);
            }
        }

        public ActionResult Register()
        {
            ViewBag.States = State.List();
            return View(new RegisterViewModel());
        }

        [HttpPost]
        public ActionResult Register(RegisterViewModel reg)
        {
            if (ModelState.IsValid)
            {
                ViewBag.States = State.List();
                SummaryViewModel summary = new SummaryViewModel();
                summary.Customer = reg.Customer;
                TempData["summary"] = summary;
                return RedirectToAction("Summary");
            }
            else
            {
                ViewBag.States = State.List();                
                return View(reg);
            }
        }

        public ActionResult Summary()
        {
            ViewBag.States = State.List();
            SummaryViewModel summary = TempData["summary"] as SummaryViewModel;
            return View(summary);
        }

        [HttpPost]
        public ActionResult Summary(SummaryViewModel summary)
        {            
            if (ModelState.IsValid)
            {
                using (Assignment3Context context = new Assignment3Context())
                {
                    context.Customers.Add(summary.Customer);
                    context.SaveChanges();
                }
                ViewBag.Message = "Register Successfully!";
                ViewBag.States = State.List();
                return View(summary);
            }
            else
                return View("Error");
        }

        public ActionResult Delete(int id)
        {
            CustomerDeleteViewModel model = new CustomerDeleteViewModel();
            try
            {
                using (Assignment3Context context = new Assignment3Context())
                {
                    Customer customer = context.Customers.Find(id);
                    context.Customers.Remove(customer);
                    context.SaveChanges();
                }
                model.IsError = false;
                model.Message = "Customer Deleted.";                
                model.Id = id;
            }

            catch (Exception e)
            {
                model.IsError = true;
                model.Message = "Error: " + e.Message;                
                model.Id = id;
            }
            return View(model);
        }

        public ActionResult Edit(int id)
        {
            CustomerEditViewModel model = new CustomerEditViewModel();
            model.IsError = false;
            try
            {

                model.Title = "Edit Customer";
                using (Assignment3Context context = new Assignment3Context())
                {

                    var customer = from s in context.Customers
                                  where s.Id == id
                                  select s;
                    model.EditableCustomer = customer.FirstOrDefault();
                }
                return View(model);
            }
            catch (Exception e)
            {
                model.IsError = true;
                model.Message = "Error: " + e.Message;
                return View(model);
            }
        }

        public ActionResult Update(Customer customer)
        {
            CustomerDeleteViewModel model = new CustomerDeleteViewModel();
            try
            {
                using (Assignment3Context context = new Assignment3Context())
                {

                    Customer original = context.Customers.Find(customer.Id);
                    context.Entry(original).CurrentValues.SetValues(customer);
                    context.SaveChanges();
                }
                model.IsError = false;
                model.Message = "Customer Updated.";                
                model.Id = customer.Id;
            }

            catch (Exception e)
            {
                model.IsError = true;
                model.Message = "Error: " + e.Message;
                model.Id = customer.Id;
            }
            return View(model);
        }
    }
}