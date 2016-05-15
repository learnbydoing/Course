using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using Assignment4.Models.DTO;
using ECTDBDal;
using ECTDBDal.Model;
using System.Net.Http;
using System.Net;

namespace Assignment4.ControllerApis
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
        public List<ProductDTO> Get([FromUri] Category category)
        {
            using (ECTDBContext context = new ECTDBContext())
            {
                if (category.CategoryId == 0)
                {
                    return context.Products
                    .Select(s => new ProductDTO { ProductId = s.ProductId, ProductName = s.ProductName, CategoryId = s.CategoryId, Price = s.Price, Image = s.Image, Condition = s.Condition, Discount = s.Discount })
                    .ToList();
                }
                else
                {
                    return context.Products.Where(c => c.CategoryId == category.CategoryId)
                        .Select(s => new ProductDTO { ProductId = s.ProductId, ProductName = s.ProductName, CategoryId = s.CategoryId, Price = s.Price, Image = s.Image, Condition = s.Condition, Discount = s.Discount })
                        .ToList();
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

        // POST api/<controller>
        public HttpResponseMessage Post([FromBody]ProductDTO value)
        {
            using (ECTDBContext context = new ECTDBContext())
            {
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