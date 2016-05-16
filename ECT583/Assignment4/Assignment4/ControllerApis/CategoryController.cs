using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Http;
using Assignment4.Models.DTO;
using Assignment4;
using ECTDBDal;
using ECTDBDal.Model;
using System.Net.Http;
using System.Net;

namespace Assignment4.ControllerApis
{
    public class CategoryController : ApiController
    {
        // GET api/<controller>
        public List<CategoryDTO> Get()
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                return context.Categories.Select(s => new CategoryDTO { CategoryId = s.CategoryId, CategoryName = s.CategoryName }).ToList();
            }
        }        

        // GET api/<controller>/5
        public CategoryDTO Get(int id)
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                Category category = context.Categories.Find(id);
                if (category == null)
                {
                    return null;
                }
                else
                {
                    return new CategoryDTO { CategoryId = category.CategoryId, CategoryName = category.CategoryName };
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

            using (ECTDBContext context = new ECTDBContext())
            {
                bool exist = context.Categories.Any(c => c.CategoryName.Equals(value.CategoryName, StringComparison.OrdinalIgnoreCase));
                if (exist)
                {
                    //var response = new HttpResponseMessage(HttpStatusCode.InternalServerError);
                    //response.Content = new StringContent("Category is already existed, please try another name!");
                    //return Request.CreateResponse(response);
                    return Request.CreateResponse(HttpStatusCode.OK, "Category is already existed, please try another name!");
                }
                Category newCategory = context.Categories.Create();
                newCategory.CategoryName = value.CategoryName;
                context.Categories.Add(newCategory);
                context.SaveChanges();
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }

        //PUT REMOVED
        // POST api/<controller>
        public HttpResponseMessage Put([FromBody]CategoryDTO value)
        {
            if (value == null || String.IsNullOrEmpty(value.CategoryName))
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Category Name can't be empty!");
            }

            using (ECTDBContext context = new ECTDBContext())
            {
                bool exist = context.Categories.Any(c => c.CategoryId == value.CategoryId);
                if (!exist)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Category["+value.CategoryId+"] does not exist!");
                }
                var category = context.Categories.Find(value.CategoryId);
                category.CategoryName = value.CategoryName;
                context.SaveChanges();
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }

        // DELETE api/<controller>/5
        public HttpResponseMessage Delete(int id)
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                var category = context.Categories.Find(id);
                context.Categories.Remove(category);
                context.SaveChanges();
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }
    }
}