import operator

import pymongo
from itertools import islice
from pymongo import MongoClient

class Database():
    client = MongoClient('mongodb://dbuser1:dbuser1dbuser1@localhost:27200/space_exploration')

    confirmed_planets_collection = client.space_exploration.confirmed_planets
    observatories_collection = client.space_exploration.observatories

    def getAllObservatories(self):
        col = Database.confirmed_planets_collection.find()
        for a in col:
            print(a)
        return self.confirmed_planets_collection.find()

    def getTopFacilitiesId(self, quantity):

        res = self.confirmed_planets_collection.aggregate([{
            "$group": {"_id": "$pl_facility_id", "count": {"$sum": 1}}
        }, {
            "$sort": {"count": -1}
        }, {
            "$limit": quantity
        }])

        return list(res)

    def getTopFacilitiesByIdArray(self, id_array):
        names_array = []
        for facility in id_array:
            names_array.append((self.getFacilityById(facility["_id"]))["name"].split('\n')[0])

        facilities_array = []
        counter = 0
        for facility in names_array:
            facilities_array.append({"name": facility, "count": id_array[counter]["count"]})
            print(counter)
            counter += 1

        return facilities_array


    def getTopLocationsByIdArray(self, id_array):

        country_names_list = []

        for facility in id_array:
            facility = self.getFacilityById(facility["_id"])
            name = facility["location"].split(',')[-1]
            if str(name) not in country_names_list:
                country_names_list.append(name)

        country_array = dict.fromkeys(country_names_list, 0)

        for facility_desc in id_array:
            facility = self.getFacilityById(facility_desc["_id"])
            country_array[str(facility["location"].split(',')[-1])] += int(facility_desc["count"])

        return sorted(country_array.items(), key=operator.itemgetter(1), reverse=True)


    def getFacilityById(self, id):
        return self.observatories_collection.find_one({"_id": id})

    def getFacilityQuantityById(self, id):
        return self.confirmed_planets_collection.find({"pl_facility_id": id}).count()

    def getFacilityQuantitiesFromArray(self, id_array):
        quantities = []
        for id in id_array:
            quantities.append(self.getFacilityQuantityById(id))

        return quantities

    # returns list of locations grouped by pl_locale
    # input: id_array - array of ids of discovering facilities
    # output: list of grouped by pl_locale planets
    def getLocationsByDiscoveryArray(self, id_array):

        country_names_list = []

        for facility in id_array:
            facility = self.getFacilityById(facility["_id"])
            name = facility["location"].split(',')[-1]
            if str(name) not in country_names_list:
                country_names_list.append(name)

        country_array = dict.fromkeys(country_names_list, 0)

        for facility_desc in id_array:
            facility = self.getFacilityById(facility_desc["_id"])
            country_array[str(facility["location"].split(',')[-1])] += int(facility_desc["count"])

        return sorted(country_array.items(), key=operator.itemgetter(1), reverse=True)


    # returns list of facilities ids by pl_locale
    # input: pl_locale (possibles are: Ground and Space)
    # output: list of grouped by pl_locale planets id's
    def getFacilitiesIdByDiscoveryMethod(self, pl_locale):

        self.confirmed_planets_collection.create_index([('pl_locale', pymongo.TEXT)], name='search_index', default_language='english')

        res = self.confirmed_planets_collection.aggregate([
            {"$match": { "$text": { "$search": pl_locale}}},
            {"$group": {"_id": "$pl_facility_id", "count": {"$sum": 1}}},
            {"$sort": {"count": -1}}
        ])

        return list(res)


    # returns top of locations by quantity of discovered planets
    # input: locations quantity
    # output: dictionary with country name as a key and quantity
    # of discovered planets as a value
    def getTopLocations(self, quantity):

        # 31 - is a quantity of all facilities that discovered planets
        ids = self.getTopFacilitiesId(31)
        return list(islice(self.getTopLocationsByIdArray(ids), quantity))

    # returns top of facilities by quantity of discovered planets
    # input: facilities quantity
    # output: list with "name" and "count" parameters in objects
    def getTopObservatories(self, quantity):
        ids = self.getTopFacilitiesId(quantity)
        return self.getTopFacilitiesByIdArray(ids)

    # returns list of locations grouped by pl_locale
    # input: id_array - array of ids of discovering facilities;
    #        quantity - quantity of top needed locations
    # output: list of grouped by pl_locale planets
    def getLocationsByPlLocale(self, pl_locale, quantity):
        ids = self.getFacilitiesIdByDiscoveryMethod(pl_locale)
        return list(islice(self.getTopLocationsByIdArray(ids), quantity))
