using GameStore.Domain;
using GameStore.Domain.Model;
using GameStore.WebUI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
{
    public class AccountController : Controller
    {
        // GET: Account
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        [AllowAnonymous]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Register(RegisterViewModel model)
        {
            if (ModelState.IsValid)
            {
                //using (GameStoreDBContext context = new GameStoreDBContext())
                //{
                //    bool exist = context.Users.Any(c => c.Email.Equals(model.Email, StringComparison.OrdinalIgnoreCase));
                //    if (exist)
                //    {
                //        ViewBag.Message = "Email address has already been registered!";
                //        return View(model);
                //    }
                //    User newUser = context.Users.Create();
                //    newUser.RoleId = 1;.CategoryName = value.CategoryName;
                //    context.Categories.Add(newCategory);
                //    context.SaveChanges();
                //    HttpContext.Current.Cache.Remove("CategoryList");
                //    return Request.CreateResponse(HttpStatusCode.OK, "Okay");
                //}

                //var user = new ApplicationUser { UserName = model.Email, Email = model.Email };
                //var result = await UserManager.CreateAsync(user, model.Password);
                //if (result.Succeeded)
                //{
                //    var newUser = UserManager.FindByEmail(model.Email);
                //    var identity = await UserManager.CreateIdentityAsync(newUser, DefaultAuthenticationTypes.ApplicationCookie);
                //    AuthenticationManager.SignIn(new AuthenticationProperties() { IsPersistent = false }, identity);


                //    // For more information on how to enable account confirmation and password reset please visit http://go.microsoft.com/fwlink/?LinkID=320771
                //    // Send an email with this link
                //    // string code = await UserManager.GenerateEmailConfirmationTokenAsync(user.Id);
                //    // var callbackUrl = Url.Action("ConfirmEmail", "Account", new { userId = user.Id, code = code }, protocol: Request.Url.Scheme);
                //    // await UserManager.SendEmailAsync(user.Id, "Confirm your account", "Please confirm your account by clicking <a href=\"" + callbackUrl + "\">here</a>");

                //    return RedirectToAction("Index", "Home");
                //}
                
            }

            // If we got this far, something failed, redisplay form
            return View(model);
        }
    }
}