﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

namespace DataServer.Models
{
    //[DataContractAttribute]
    public class Restaurant
    {
        //[DataContractAttribute]
        public enum CategoryType {Restaurant, Dessert, CoffeeTea, Bakeries, IceCream}

        public Restaurant()
        {

        }
        public Restaurant(String name, CategoryType category, String location, float rating, int reviews, String image, String image1, String image2, String image3)
        //public Restaurant(String name, String location, float rating, int reviews, String image, String image1, String image2, String image3)
        {
            this.Name = name;
            this.Category = category;
            this.Location = location;
            this.Rating = rating;
            this.Reviews = reviews;
            this.Image = image;
            this.Image1 = image1;
            this.Image2 = image2;
            this.Image3 = image3;
        }

        public string Name { get; set; }
        public CategoryType Category { get; set; }
        public string Location { get; set; }
        public float Rating { get; set; }
        public int Reviews { get; set; }
        public string Image { get; set; }
        public string Image1 { get; set; }
        public string Image2 { get; set; }
        public string Image3 { get; set; }
    }
}