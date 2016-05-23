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
    [Authorize(Roles = "Admin")]
    public class CategoryController : Controller
    {
        // GET: Category
        public ActionResult Index()
        {
            return View();
        }        
    }
}