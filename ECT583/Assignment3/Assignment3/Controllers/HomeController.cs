using Assignment3.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Assignment3.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Register()
        {
            ViewBag.States = State.List();
            return View(new RegisterViewModel() { State = "AL" });
        }

        [HttpPost]
        public ActionResult Register(RegisterViewModel reg)
        {
            if (ModelState.IsValid)
            {
                ViewBag.States = State.List();
                return View("Summary", reg);
            }
            else
            {
                ViewBag.States = State.List();                
                return View(reg);
            }
        }

        public ActionResult Summary(RegisterViewModel summary)
        {
            ViewBag.States = State.List();
            return View(summary);
        }
    }
}