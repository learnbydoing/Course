using GameStore.Domain.Identity;
using GameStore.Domain.Infrastructure;
using GameStore.WebUI.Models;
using GameStore.WebUI.Models.DTO;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.Owin.Security;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;

namespace GameStore.WebUI.Apis
{
    [Authorize(Roles = "Admin")]
    public class UserController : BaseApiController
    {
        // GET api/<controller>
        public List<AppUser> Get()
        {
            if (HttpContext.Current.Cache["UserList"] != null)
            {
                return (List<AppUser>)HttpContext.Current.Cache["UserList"];
            }
            else
            {
                List<AppUser> users = UserManager.Users.ToList();
                HttpContext.Current.Cache["UserList"] = users;
                return users;
            }
        }

        // GET api/<controller>/5
        public AppUser Get(string id)
        {
            if (HttpContext.Current.Cache["User" + id] != null)
            {
                return (AppUser)HttpContext.Current.Cache["User" + id];
            }
            else
            {
                AppUser user = UserManager.FindById(id);
                HttpContext.Current.Cache["User" + id] = user;
                return user;
            }            
        }

        // GET: api/Category/GetCount/
        [Route("api/User/GetCount")]
        public int GetCount()
        {
            if (HttpContext.Current.Cache["UserList"] != null)
            {
                List<AppUser> list = (List<AppUser>)HttpContext.Current.Cache["UserList"];
                return list.Count();
            }
            else
            {
                List<AppUser> users = UserManager.Users.ToList();
                HttpContext.Current.Cache["UserList"] = users;
                return users.Count();
            }
        }

        [Route("api/User/Create")]
        public async Task<HttpResponseMessage> Create([FromBody]AppUser model)
        {
            if (ModelState.IsValid)
            {
                var user = new AppUser { UserName = model.Email, Email = model.Email, PhoneNumber = model.PhoneNumber, Membership = model.Membership };
                var result = await UserManager.CreateAsync(user, "asdasd");
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("UserList");
                    return Request.CreateResponse(HttpStatusCode.OK, "Okay");
                }
                else
                {
                    return Request.CreateResponse(HttpStatusCode.OK, GetErrorMessage(result));
                }                            
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.OK, "ModelState.IsValid=false");
            }
        }
        public HttpResponseMessage Post([FromBody]AppUser user)
        {
            if (ModelState.IsValid)
            {
                AppUser existUser = UserManager.FindById(user.Id);
                if (existUser == null)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "User [" + user.Id + "] does not exist!");
                }
                existUser.PhoneNumber = user.PhoneNumber;
                existUser.Membership = user.Membership;
                //existUser. = role.Description;
                existUser.Roles.Clear();
                var role = RoleManager.Roles.Where(r => r.Name == user.Membership).First();
                existUser.Roles.Add(new IdentityUserRole { RoleId = role.Id, UserId = existUser.Id });
                IdentityResult result = UserManager.Update(existUser);
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("UserList");
                    HttpContext.Current.Cache.Remove("User" + user.Id);
                    return Request.CreateResponse(HttpStatusCode.OK, "Okay");
                }
                else
                {
                    return Request.CreateResponse(HttpStatusCode.OK, GetErrorMessage(result));
                }
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.OK, "ModelState.IsValid=false");
            }            
        }
        
        // DELETE api/<controller>/5
        public HttpResponseMessage Delete(string id)
        {
            AppUser user = UserManager.FindById(id);
            if (user == null)
            {
                return Request.CreateResponse(HttpStatusCode.OK, "User ["+id+"] not found.");
            }            
            else
            {
                IdentityResult result = UserManager.Delete(user);
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("UserList");
                    HttpContext.Current.Cache.Remove("User" + user.Id);
                    return Request.CreateResponse(HttpStatusCode.OK, "Okay");
                }
                else
                {
                    return Request.CreateResponse(HttpStatusCode.OK, GetErrorMessage(result));
                }
            }            
        }
    }
}
