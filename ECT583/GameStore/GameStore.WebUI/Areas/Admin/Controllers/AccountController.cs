using GameStore.WebUI.Areas.Admin.Models;
using GameStore.WebUI.Models.DTO;
using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GameStore.Domain.Identity;
using GameStore.WebUI.Controllers;

namespace GameStore.WebUI.Areas.Admin.Controllers
{
    [Authorize(Roles = "Admin")]
    public class AccountController : BaseController
    {
        // GET: User
        public ActionResult AppUser()
        {
            ViewBag.Roles = RoleManager.Roles.ToList();

            UserViewModel rolevm = new UserViewModel();
            rolevm.CanAdd = true;
            return View(rolevm);
        }
        public ActionResult AppRole()
        {
            RoleViewModel rolevm = new RoleViewModel();
            rolevm.CanAdd = true;
            return View(rolevm);
        }
    }
}