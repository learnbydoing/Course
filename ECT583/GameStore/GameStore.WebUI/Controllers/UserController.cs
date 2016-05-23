using GameStore.WebUI.Models;
using GameStore.WebUI.Models.DTO;
using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GameStore.Domain.Identity;

namespace GameStore.WebUI.Controllers
{
    [Authorize(Roles = "Admin")]
    public class UserController : BaseController
    {
        // GET: User
        public ActionResult Index()
        {
            List<AppRole> list = null;
            if (System.Web.HttpContext.Current.Cache["RoleList"] != null)
            {
                list = (List<AppRole>)System.Web.HttpContext.Current.Cache["RoleList"];
            }
            else
            {
                list = RoleManager.Roles.ToList();
                System.Web.HttpContext.Current.Cache["RoleList"] = list;                
            }
            ViewBag.Roles = list;

            UserViewModel rolevm = new UserViewModel();
            rolevm.CanAdd = true;
            return View(rolevm);
        }        
    }
}