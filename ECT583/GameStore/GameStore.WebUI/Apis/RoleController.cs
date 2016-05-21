using GameStore.Domain.Identity;
using GameStore.Domain.Infrastructure;
using GameStore.WebUI.Models.DTO;
using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace GameStore.WebUI.Apis
{
    public class RoleController : BaseApiController
    {
        // GET api/<controller>
        public List<AppRole> Get()
        {
            if (HttpContext.Current.Cache["RoleList"] != null)
            {
                return (List<AppRole>)HttpContext.Current.Cache["RoleList"];
            }
            else
            {
                List<AppRole> roles = RoleManager.Roles.ToList();
                HttpContext.Current.Cache["RoleList"] = roles;
                return roles;
            }
        }

        // GET api/<controller>/5
        public AppRole Get(string id)
        {
            if (HttpContext.Current.Cache["Role" + id] != null)
            {
                return (AppRole)HttpContext.Current.Cache["Role" + id];
            }
            else
            {
                AppRole role = RoleManager.FindById(id);
                HttpContext.Current.Cache["Role" + id] = role;
                return role;
            }            
        }

        // GET: api/Category/GetCount/
        [Route("api/Role/GetCount")]
        public int GetCount()
        {
            if (HttpContext.Current.Cache["RoleList"] != null)
            {
                List<AppRole> list = (List<AppRole>)HttpContext.Current.Cache["RoleList"];
                return list.Count();
            }
            else
            {
                List<AppRole> roles = RoleManager.Roles.ToList();
                HttpContext.Current.Cache["RoleList"] = roles;
                return roles.Count();
            }
        }

        [Route("api/Role/Create")]
        public HttpResponseMessage Create([FromBody]AppRole role)
        {
            if (ModelState.IsValid)
            {
                AppRole existRole = RoleManager.FindByName(role.Name);
                if (existRole != null)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Role [" + role.Name + "] is already existed, please try another name!");
                }
                IdentityResult result = RoleManager.Create(role);
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("RoleList");
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
        public HttpResponseMessage Post([FromBody]AppRole role)
        {
            if (ModelState.IsValid)
            {
                AppRole existRole = RoleManager.FindById(role.Id);
                if (existRole == null)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Role [" + role.Id + "] does not exist!");
                }
                existRole.Name = role.Name;
                existRole.Description = role.Description;
                IdentityResult result = RoleManager.Update(existRole);
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("RoleList");
                    HttpContext.Current.Cache.Remove("Role" + role.Id);
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
            AppRole role = RoleManager.FindById(id);
            if (role == null)
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Role ["+id+"] not found.");
            }
            else if (role.Users.Count > 0)
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Users are assigned with role [" + role.Name + "], remove them first!");
            }
            else
            {
                IdentityResult result = RoleManager.Delete(role);
                if (result.Succeeded)
                {
                    HttpContext.Current.Cache.Remove("RoleList");
                    HttpContext.Current.Cache.Remove("Role" + id);
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
