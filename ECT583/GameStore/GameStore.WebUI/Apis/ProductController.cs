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
            if (value.CategoryId == 0)
            {
                if (HttpContext.Current.Cache["ProductList"] != null)
                    return (List<ProductDTO>)HttpContext.Current.Cache["ProductList"];
            }
            else
            {
                if (HttpContext.Current.Cache["ProductList" + value.CategoryId] != null)
                    return (List<ProductDTO>)HttpContext.Current.Cache["ProductList" + value.CategoryId];
            }
                
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                if (value.CategoryId == 0)
                {
                    var query = from product in context.Products
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                      select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };

                    List<ProductDTO> products = query.ToList();
                    HttpContext.Current.Cache["ProductList"] = products;
                    return products;
                }
                else
                {
                    var query = from product in context.Products
                                where product.CategoryId == value.CategoryId
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                                select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                    List<ProductDTO> products = query.ToList();
                    HttpContext.Current.Cache["ProductList" + value.CategoryId] = products;
                    return products;                 
                }
            }
        }

        // GET api/<controller>/5
        public ProductDTO Get(int id)
        {
            if (HttpContext.Current.Cache["Product" + id] != null)
                return (ProductDTO)HttpContext.Current.Cache["Product" + id];
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                Product product = context.Products.Find(id);
                if (product == null)
                {
                    return null;
                }
                else
                {
                    ProductDTO productDTO = new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };
                    HttpContext.Current.Cache["Product" + id] = productDTO;
                    return productDTO;
                }
            }

        }

        // GET: api/Product/GetCount/
        [Route("api/Product/GetCount")]
        public int GetCount()
        {
            if (HttpContext.Current.Cache["ProductList"] != null)
            {
                List<ProductDTO> list = (List<ProductDTO>)HttpContext.Current.Cache["ProductList"];
                return list.Count();
            }
            else
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    var query = from product in context.Products
                                join category in context.Categories
                                  on product.CategoryId equals category.CategoryId
                                select new ProductDTO { ProductId = product.ProductId, ProductName = product.ProductName, CategoryId = product.CategoryId, CategoryName = category.CategoryName, Price = product.Price, Image = product.Image, Condition = product.Condition, Discount = product.Discount };

                    List<ProductDTO> products = query.ToList();
                    HttpContext.Current.Cache["ProductList"] = products;
                    return products.Count();
                }
            }
        }

        // POST api/<controller>
        public HttpResponseMessage Post([FromBody]ProductDTO value)
        {
            if (value.Discount < 0 || value.Discount > 100)
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Discount must between 0 ~ 100.");
            }
            using (GameStoreDBContext context = new GameStoreDBContext())
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
                HttpContext.Current.Cache.Remove("ProductList");
                HttpContext.Current.Cache.Remove("ProductList" + newProduct.CategoryId);                
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }

        //PUT REMOVED

        // DELETE api/<controller>/5
        public HttpResponseMessage Delete(int id)
        {
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                var product = context.Products.Find(id);
                context.Products.Remove(product);
                context.SaveChanges();
                HttpContext.Current.Cache.Remove("ProductList");
                HttpContext.Current.Cache.Remove("ProductList" + product.CategoryId);
                HttpContext.Current.Cache.Remove("Product" + id);
                return Request.CreateResponse(HttpStatusCode.OK, "Okay");
            }
        }
    }
}