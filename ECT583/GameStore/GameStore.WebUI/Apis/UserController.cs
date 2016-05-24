using GameStore.Domain.Identity;
using GameStore.Domain.Infrastructure;
using GameStore.WebUI.Areas.Admin.Models;
using GameStore.WebUI.Areas.Admin.Models.DTO;
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
        public List<UserDTO> Get()
        {
            if (HttpContext.Current.Cache["UserList"] != null)
            {
                return (List<UserDTO>)HttpContext.Current.Cache["UserList"];
            }
            else
            {
                var users = UserManager.Users.ToList();
                List<UserDTO> list = users.Select(u => new UserDTO { Id = u.Id, Email = u.Email, UserName = u.UserName, Membership = u.Membership }).ToList();
                HttpContext.Current.Cache["UserList"] = list;
                return list;
            }
        }

        // GET api/<controller>/5
        public UserDTO Get(string id)
        {
            if (HttpContext.Current.Cache["User" + id] != null)
            {
                return (UserDTO)HttpContext.Current.Cache["User" + id];
            }
            else
            {
                AppUser u = UserManager.FindById(id);
                UserDTO user = new UserDTO { Id = u.Id, Email = u.Email, UserName = u.UserName, Membership = u.Membership };
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
                List<UserDTO> list = (List<UserDTO>)HttpContext.Current.Cache["UserList"];
                return list.Count();
            }
            else
            {
                var users = UserManager.Users.ToList();
                List<UserDTO> list = users.Select(u => new UserDTO { Id = u.Id, Email = u.Email, UserName = u.UserName, Membership = u.Membership }).ToList();
                HttpContext.Current.Cache["UserList"] = list;
                return users.Count();
            }
        }

        [Route("api/User/Create")]
        public async Task<HttpResponseMessage> Create([FromBody]UserViewModel value)
        {
            if (ModelState.IsValid)
            {
                var user = new AppUser { Email = value.Email, UserName = value.UserName, Membership = value.Membership };
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
        public HttpResponseMessage Post([FromBody]UserViewModel value)
        {
            if (ModelState.IsValid)
            {
                AppUser user = UserManager.FindById(value.Id);
                if (user == null)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "User [" + value.Id + "] does not exist!");
                }
                user.Membership = value.Membership;
                //existUser. = role.Description;
                user.Roles.Clear();
                var role = RoleManager.Roles.Where(r => r.Name == value.Membership).First();
                user.Roles.Add(new IdentityUserRole { RoleId = role.Id, UserId = user.Id });
                IdentityResult result = UserManager.Update(user);
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("UserList");
                    HttpContext.Current.Cache.Remove("User" + value.Id);
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
