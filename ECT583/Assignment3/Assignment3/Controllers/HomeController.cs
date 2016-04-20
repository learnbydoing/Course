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
            CustomerListViewModel model = TempData["model"] as CustomerListViewModel;
            if (model == null)
            {
                model = new CustomerListViewModel();
                model.IsError = false;
            }
            try
            {
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
            if (summary == null || summary.Customer == null)
            {
                ViewBag.Message = "Invalid access! You must fill information in registration page first!";
                return View("Error");
            }
            else
            {
                return View(summary);
            }
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
                ViewBag.Message = "New customer [" + summary.Customer.Id.ToString() + "] has registered successfully!";
                ViewBag.States = State.List();
                return View(summary);
                //return RedirectToAction("Index", new { message = "New Customer Register!" });
            }
            else
                return View("Error");
        }

        public ActionResult Delete(int id)
        {
            CustomerListViewModel model = new CustomerListViewModel();
            try
            {
                using (Assignment3Context context = new Assignment3Context())
                {
                    Customer customer = context.Customers.Find(id);
                    context.Customers.Remove(customer);
                    context.SaveChanges();
                }

                model.IsError = false;
                model.Message = "Customer [" + id.ToString() + "] has been deleted!";                
            }
            catch (Exception e)
            {
                model.IsError = true;
                model.Message = "Error: " + e.Message;   
            }
            TempData["model"] = model;
            return RedirectToAction("Index");
        }

        public ActionResult Edit(int? id)
        {
            CustomerEditViewModel model = new CustomerEditViewModel();
            model.IsError = false;
            try
            {
                model.Title = "Edit Customer";
                if (id == null)
                {
                    model.IsError = true;
                    model.Message = "Error: " + "Invalid Parameter!";
                }
                else
                {
                    using (Assignment3Context context = new Assignment3Context())
                    {

                        var customer = from s in context.Customers
                                       where s.Id == id
                                       select s;
                        model.Customer = customer.FirstOrDefault();
                        model.Password = model.Customer.Password;
                        model.ConfirmPassword = model.Customer.Password;
                    }
                    ViewBag.States = State.List();
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
        [HttpPost]
        public ActionResult Edit(CustomerEditViewModel model)
        {
            try
            {
                using (Assignment3Context context = new Assignment3Context())
                {
                    Customer original = context.Customers.Find(model.Customer.Id);
                    context.Entry(original).CurrentValues.SetValues(model.Customer);
                    context.SaveChanges();
                }
                model.IsError = false;
                model.Message = "Customer Updated.";                
            }

            catch (Exception e)
            {
                model.IsError = true;
                model.Message = "Error: " + e.Message;
            }
            ViewBag.States = State.List();
            return View(model);
        }
    }
}