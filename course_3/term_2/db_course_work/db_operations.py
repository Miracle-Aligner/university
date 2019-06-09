import operator

import pymongo
import numpy as np
from itertools import islice
from pymongo import MongoClient
from statsmodels.tsa.ar_model import AR


class Database():
    client = MongoClient('mongodb://dbuser1:dbuser1dbuser1@localhost:27200/space_exploration')

    confirmed_planets_collection = client.space_exploration.confirmed_planets
    observatories_collection = client.space_exploration.observatories

    def getAllObservatories(self):
        return self.confirmed_planets_collection.find()

    def get_disc_years(self, pl_locale):
        return self.confirmed_planets_collection.aggregate(
           [
                {
                    "$match": {
                        "pl_locale": pl_locale
                    }
                },
              {
                "$group" : {
                   "_id" : "$pl_disc",
                   "count": { "$sum": 1 }
                }
              },
               {
                   "$sort": {
                       "_id": 1
                   }
               }
           ]
        )

    # returns Cursor object of discovery methods of planets which controversial flag == pl_controvflag
    def get_contr_methods(self, pl_controvflag):
        return self.confirmed_planets_collection.aggregate(
           [
                {
                    "$match": {
                        "pl_controvflag": pl_controvflag
                    }
                },
              {
                "$group" : {
                   "_id" : "$pl_discmethod",
                   "count": { "$sum": 1 }
                }
              },
               {
                   "$sort": {
                       "_id": 1
                   }
               }
           ]
        )

    def get_discovery_methods(self):
        return self.confirmed_planets_collection.aggregate(
           [
              {
                "$group" : {
                   "_id" : "$pl_discmethod",
                   "count": { "$sum": 1 }
                }
              },
               {
                   "$sort": {
                       "_id": 1
                   }
               }
           ]
        )

    def get_discovery_accuracy_stat(self, pl_controvflag):
        controv_methods = self.get_contr_methods(pl_controvflag)
        all_methods = self.get_discovery_methods()

        #method_stats = np.array([])
        #method_counts = np.array([])
        # method_names = np.array([])
        #for obj in controv_methods:
            #method_stats = np.append(method_stats, [obj["_id"], obj["count"]], dtype=[('name', 'U50'), ('quantity', 'i4')])
        #print(method_stats)

        flagged_methods_dict = {}
        for obj in controv_methods:
            flagged_methods_dict[obj["_id"]] = obj["count"]

        general_methods_dict = {}
        for obj in all_methods:
            general_methods_dict[obj["_id"]] = obj["count"]

        for key, value in general_methods_dict.items():
            if key in flagged_methods_dict:
                general_methods_dict[key] = 100 * (flagged_methods_dict[key] / value)
            else:
                general_methods_dict[key] = 0.0


        names = ['name', 'accuracy']
        formats = ['U50', 'f8']
        dtype = dict(names=names, formats=formats)
        np_result = np.array(list(general_methods_dict.items()), dtype=dtype)

        return(np_result)

    def get_discovery_stat(self, pl_locale):
        data = self.get_disc_years(pl_locale)

        list_counts = list()
        years = list()
        for obj in data:
            years.append(obj["_id"])
            list_counts.append(obj["count"])


        return {"years": years, "counts": list_counts}

    def get_top_facilities_id(self, quantity):

        res = self.confirmed_planets_collection.aggregate([{
            "$group": {"_id": "$pl_facility_id", "count": {"$sum": 1}}
        }, {
            "$sort": {"count": -1}
        }, {
            "$limit": quantity
        }])

        return list(res)

    def get_top_facilities_by_id_array(self, id_array):
        names_array = []
        for facility in id_array:
            names_array.append((self.get_facility_by_id(facility["_id"]))["name"].split('\n')[0])

        facilities_array = dict.fromkeys(names_array, 0)

        for facility_desc in id_array:
            facility = self.get_facility_by_id(facility_desc["_id"])
            facilities_array[str(facility["name"].split(',')[-1])] += int(facility_desc["count"])

        sorted_facilities = sorted(facilities_array.items(), key=operator.itemgetter(1), reverse=True)

        return np.array(sorted_facilities[:], dtype=[('name', 'U50'), ('quantity', 'i4')])


    def get_top_locations_by_id_array(self, id_array):

        country_names_list = []

        for facility in id_array:
            facility = self.get_facility_by_id(facility["_id"])
            name = facility["location"].split(',')[-1]
            if str(name) not in country_names_list:
                country_names_list.append(name)

        country_array = dict.fromkeys(country_names_list, 0)

        for facility_desc in id_array:
            facility = self.get_facility_by_id(facility_desc["_id"])
            country_array[str(facility["location"].split(',')[-1])] += int(facility_desc["count"])

        # sort locations
        sorted_locations = sorted(country_array.items(), key=operator.itemgetter(1), reverse=True)

        # returns numpy array

        return np.array(sorted_locations[:], dtype=[('name', 'U50'), ('quantity', 'i4')])


    def get_facility_by_id(self, id):
        return self.observatories_collection.find_one({"_id": id})

    def get_facility_quantity_by_id(self, id):
        return self.confirmed_planets_collection.find({"pl_facility_id": id}).count()

    def get_facility_quantities_from_array(self, id_array):
        quantities = []
        for id in id_array:
            quantities.append(self.get_facility_quantity_by_id(id))

        return quantities

    # returns list of locations grouped by pl_locale
    # input: id_array - array of ids of discovering facilities
    # output: list of grouped by pl_locale planets
    def get_locations_by_discovery_array(self, id_array):

        country_names_list = []

        for facility in id_array:
            facility = self.get_facility_by_id(facility["_id"])
            name = facility["location"].split(',')[-1]
            if str(name) not in country_names_list:
                country_names_list.append(name)

        country_array = dict.fromkeys(country_names_list, 0)

        for facility_desc in id_array:
            facility = self.get_facility_by_id(facility_desc["_id"])
            country_array[str(facility["location"].split(',')[-1])] += int(facility_desc["count"])

        return sorted(country_array.items(), key=operator.itemgetter(1), reverse=True)


    # returns list of facilities ids by pl_locale
    # input: pl_locale (possibles are: Ground and Space)
    # output: list of grouped by pl_locale planets id's
    def get_facilities_id_by_discovery_method(self, pl_locale):

        self.confirmed_planets_collection.create_index([('pl_locale', pymongo.TEXT)], name='search_index', default_language='english')

        res = self.confirmed_planets_collection.aggregate([
            {"$match": { "$text": { "$search": pl_locale}}},
            {"$group": {"_id": "$pl_facility_id", "count": {"$sum": 1}}},
            {"$sort": {"count": -1}}
        ])

        return list(res)

    def get_array_with_percents(self, locations):

        return locations['quantity'][0:] * 100 / locations['quantity'][0:].sum()

    # returns dictionary with top of locations by percent
    # of discovered planets among all discovered planets
    # input: locations quantity
    # output: dictionary with country name as a key and quantity
    # of discovered planets as a value
    def get_top_locations_by_percentage(self, quantity):
        ids = self.get_top_facilities_id(quantity)
        locations = self.get_top_locations_by_id_array(ids)
        percents_array = self.get_array_with_percents(locations)
        return {"x": locations['name'], "y": percents_array}

    # returns dictionary with top of locations by quantity of discovered planets
    # input: locations quantity
    # output: dictionary with country name as a key and quantity
    # of discovered planets as a value
    def get_top_locations(self, quantity):

        ids = self.get_top_facilities_id(quantity)
        locations = self.get_top_locations_by_id_array(ids)

        return {"x": locations['name'], "y": locations['quantity']}

    # returns top of facilities by quantity of discovered planets
    # input: facilities quantity
    # output: list with "name" and "count" parameters in objects
    def get_top_facilities(self, quantity):

        ids = self.get_top_facilities_id(quantity)
        facilities = self.get_top_facilities_by_id_array(ids)

        return {"x": facilities['name'], "y": facilities['quantity']}

    # returns list of locations grouped by pl_locale
    # input: id_array - array of ids of discovering facilities;
    #        quantity - quantity of top needed locations
    # output: list of grouped by pl_locale planets
    def getLocationsByPlLocale(self, pl_locale, quantity):
        ids = self.get_facilities_id_by_discovery_method(pl_locale)
        return list(islice(self.get_top_locations_by_id_array(ids), quantity))

    def predict_f_discovered_planets_quantity(self):

        stat = self.get_discovery_stat("Ground")["counts"]
        # fit model
        model = AR(stat)
        model_fit = model.fit()
        # make prediction
        yhat = model_fit.predict(len(stat), len(stat))
        return yhat





