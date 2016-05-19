using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using Assignment5.Models.DTO;
using ECTDBDal;
using ECTDBDal.Model;
using System.Net.Http;
using System.Net;

namespace Assignment5.ControllerApis
{
    public class ProductController : ApiController
    {
        // GET api/<controller>
        //public List<ProductDTO> Get()
        //{
        //    using (ECTDBContext context = new ECTDBContext())
        //    {
        //        return context.Products.Select(s => new ProductDTO { ProductId = s.ProductId, ProductName = s.ProductName, CategoryId = s.CategoryId, Price = s.Price, Image = s.Image, Condition = s.Condition, Discount = s.Discount }).ToList();
        //    }
        //}

        // GET api/<controller>
        public List<ProductDTO> Get([FromUri] Category value)
        {
            if (HttpContext.Current.Cache["ProdcutList"] != null)
                return (List<ProductDTO>)HttpContext.Current.Cache["ProdcutList"];
            using (ECTDBContext context = new ECTDBContext())
            {
                if (value.CategoryId == 0)
                {
                    var query = from product in context.Products
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                      select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                    return query.ToList();
                }
                else
                {
                    var query = from product in context.Products
                                where product.CategoryId == value.CategoryId
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                                select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                    return query.ToList();                   
                }
            }
        }

        // GET api/<controller>/5
        public ProductDTO Get(int id)
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                Product product = context.Products.Find(id);
                if (product == null)
                {
                    return null;
                }
                else
                {
                    return new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                }
            }

        }

        // GET: api/Product/GetCount/
        [Route("api/Product/GetCount")]
        public int GetCount()
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                return context.Products.Count();
            }
        }

        // POST api/<controller>
        public HttpResponseMessage Post([FromBody]ProductDTO value)
        {
            if (value.Discount < 0 || value.Discount > 100)
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Discount must between 0 ~ 100.");
            }
            using (ECTDBContext context = new ECTDBContext())
            {
                bool exist = context.Products.Any(c => c.ProductName.Equals(value.ProductName, StringComparison.OrdinalIgnoreCase));
                if (exist)
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Product [" + value.ProductName + "] is already existed, please try another name!");
                }
                Product newProduct = context.Products.Create();
                newProduct.ProductName = value.ProductName;
                newProduct.CategoryId = value.CategoryId;
                newProduct.Price = value.Price;
                newProduct.Image = value.Image;
                newProduct.Condition = value.Condition;
                newProduct.Discount = value.Discount;                
                context.Products.Add(newProduct);
                context.SaveChanges();
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }

        //PUT REMOVED

        // DELETE api/<controller>/5
        public HttpResponseMessage Delete(int id)
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                var student = context.Products.Find(id);
                context.Products.Remove(student);
                context.SaveChanges();
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }
    }
}