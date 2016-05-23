using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Web.Http;
using GameStore.WebUI.Models.DTO;
using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
using System.Net.Http;
using System.Net;

namespace GameStore.WebUI.Apis
{
    [Authorize(Roles = "Admin")]
    public class CategoryController : ApiController
    {
        // GET api/<controller>
        public List<Category> Get()
        {
            if (HttpContext.Current.Cache["CategoryList"] != null)
                return (List<Category>)HttpContext.Current.Cache["CategoryList"];
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                List<Category> categories = context.Categories.ToList();
                HttpContext.Current.Cache["CategoryList"] = categories;
                return categories;
            }
        }        

        // GET api/<controller>/5
        public Category Get(int id)
        {
            if (HttpContext.Current.Cache["Category" + id] != null)
                return (Category)HttpContext.Current.Cache["Category" + id];
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                Category category = context.Categories.Find(id);
                HttpContext.Current.Cache["Category" + id] = category;
                return category;
            }
        }

        // GET: api/Category/GetCount/
        [Route("api/Category/GetCount")]
        public int GetCount()
        {
            if (HttpContext.Current.Cache["CategoryList"] != null)
            {
                List<Category> list = (List<Category>)HttpContext.Current.Cache["CategoryList"];
                return list.Count();
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    List<Category> categories = context.Categories.ToList();
                    HttpContext.Current.Cache["CategoryList"] = categories;
                    return categories.Count();
                }
            }
        }

        // POST api/<controller>
        [Route("api/Category/Create")]
        public HttpResponseMessage Create([FromBody]Category value)
        {
            if (value==null || String.IsNullOrEmpty(value.CategoryName))
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Category Name can't be empty!");
            }            

            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                bool exist = context.Categories.Any(c => c.CategoryName.Equals(value.CategoryName, StringComparison.OrdinalIgnoreCase));
                if (exist)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Category ["+ value.CategoryName + "] is already existed, please try another name!");
                }
                Category newCategory = context.Categories.Create();
                newCategory.CategoryName = value.CategoryName;
                context.Categories.Add(newCategory);
                context.SaveChanges();
                HttpContext.Current.Cache.Remove("CategoryList");
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }

        //PUT REMOVED
        // Ajax.htmlForm does not support put and delete, only supports get and post.
        public HttpResponseMessage Post([FromBody]Category value)
        {
            if (value == null || String.IsNullOrEmpty(value.CategoryName))
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Category Name can't be empty!");
            }

            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                bool exist = context.Categories.Any(c => c.CategoryId == value.CategoryId);
                if (!exist)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Category ["+value.CategoryId+"] does not exist!");
                }
                
                exist = context.Categories.Where(c => c.CategoryId != value.CategoryId).Any(c => c.CategoryName.Equals(value.CategoryName, StringComparison.OrdinalIgnoreCase));
                if (exist)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Category [" + value.CategoryName + "] is already existed, please try another name!");
                }
                var category = context.Categories.Find(value.CategoryId);
                category.CategoryName = value.CategoryName;
                context.SaveChanges();
                HttpContext.Current.Cache.Remove("CategoryList");
                HttpContext.Current.Cache.Remove("Category" + value.CategoryId);
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }

        // DELETE api/<controller>/5
        public HttpResponseMessage Delete(int id)
        {
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                bool exist = context.Products.Any(p => p.CategoryId == id);
                if (exist)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "There are products belong to Category [" + id + "], delete them first!");
                }
                var category = context.Categories.Find(id);
                context.Categories.Remove(category);
                context.SaveChanges();
                HttpContext.Current.Cache.Remove("CategoryList");
                HttpContext.Current.Cache.Remove("Category" + id);
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }
    }
}