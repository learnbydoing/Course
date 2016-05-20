using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Web.Http;
using GameStore.WebUI.Models.DTO;
using GameStore.WebUI;
using GameStore.Domain;
using GameStore.Domain.Model;
using System.Net.Http;
using System.Net;

namespace GameStore.WebUI.ControllerApis
{
     public class CategoryController : ApiController
    {
        // GET api/<controller>
        public List<CategoryDTO> Get()
        {
            if (HttpContext.Current.Cache["CategoryList"] != null)
                return (List<CategoryDTO>)HttpContext.Current.Cache["CategoryList"];
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                List<CategoryDTO> categories = context.Categories.Select(s => new CategoryDTO { CategoryId = s.CategoryId, CategoryName = s.CategoryName }).ToList();
                HttpContext.Current.Cache["CategoryList"] = categories;
                return categories;
            }
        }        

        // GET api/<controller>/5
        public CategoryDTO Get(int id)
        {
            if (HttpContext.Current.Cache["Category" + id] != null)
                return (CategoryDTO)HttpContext.Current.Cache["Category" + id];
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                Category category = context.Categories.Find(id);
                if (category == null)
                {
                    return null;
                }
                else
                {
                    CategoryDTO categoryDTO = new CategoryDTO { CategoryId = category.CategoryId, CategoryName = category.CategoryName };
                    HttpContext.Current.Cache["Category" + id] = categoryDTO;
                    return categoryDTO;
                }
            }
        }

        // GET: api/Category/GetCount/
        [Route("api/Category/GetCount")]
        public int GetCount()
        {
            if (HttpContext.Current.Cache["CategoryList"] != null)
            {
                List<CategoryDTO> list = (List<CategoryDTO>)HttpContext.Current.Cache["CategoryList"];
                return list.Count();
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    List<CategoryDTO> categories = context.Categories.Select(s => new CategoryDTO { CategoryId = s.CategoryId, CategoryName = s.CategoryName }).ToList();
                    HttpContext.Current.Cache["CategoryList"] = categories;
                    return categories.Count();
                }
            }
        }

        // POST api/<controller>
        public HttpResponseMessage Post([FromBody]CategoryDTO value)
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
        public HttpResponseMessage Put([FromBody]CategoryDTO value)
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