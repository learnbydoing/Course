using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DataServer.Models
{
    public class RestaurantRespository
    {
        private static RestaurantRespository repo = new RestaurantRespository();

        public static RestaurantRespository Current
        {
            get
            {
                return repo;
            }
        }

        private List<Restaurant> restaurants = new List<Restaurant> {
            new Restaurant { 
                RestaurantId = 1, RestaurantName = "Johnny", Location = "jojozhuang@gmail.com", Reviews = 75, Rating = 4},
            new Restaurant { 
                RestaurantId = 2, RestaurantName = "Marlon", Location = "marlon@gmail.com", Reviews = 12, Rating = 3},
            new Restaurant { 
                RestaurantId = 3, RestaurantName = "David", Location = "david@gmail.com", Reviews = 63, Rating = 5},
        };

        public IEnumerable<Restaurant> GetList()
        {
            return restaurants;
        }

        public Restaurant Get(int id)
        {
            return restaurants.Where(r => r.RestaurantId == id).FirstOrDefault();
        }

        public Restaurant Add(Restaurant item)
        {
            item.RestaurantId = restaurants.Count + 1;
            restaurants.Add(item);
            return item;
        }

        public void Remove(int id)
        {
            Restaurant item = Get(id);
            if (item != null)
            {
                restaurants.Remove(item);
            }
        }

        public bool Update(Restaurant item)
        {
            Restaurant udpItem = Get(item.RestaurantId);
            if (udpItem != null)
            {
                udpItem.RestaurantName = item.RestaurantName;
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}