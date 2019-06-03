from collections import namedtuple

import os
import requests;
import json
import lxml
import html5lib
from bs4 import BeautifulSoup
import pymongo
from pymongo import MongoClient

class Dataset():
    def generate(self):

        w_dataset = Wiki_dataset()
        n_dataset = NASA_dataset()

        w_dataset.generate()
        n_dataset.generate()


class NASA_dataset():

    def generate(self):
        nasa_request_url = "https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?" + \
                           "table=exoplanets" + \
                           "&select=pl_hostname,pl_letter,pl_name,pl_discmethod,pl_controvflag,rowupdate,pl_facility,pl_masse,pl_locale,ra,dec" + \
                           "&order=dec" + \
                           "&format=json"

        # getting list of discovered planets
        planets_array = self.get_planets_list(nasa_request_url);

        # adding planets data to db
        self.fill_db(planets_array)


    def fill_db(self, arr):
        client = MongoClient('mongodb://dbuser1:dbuser1dbuser1@localhost:27200/space_exploration')
        confirmed_planets_collection = client.space_exploration.confirmed_planets
        for planet in arr:
            # searching for discovering facility in observatories_collection
            w_dataset = Wiki_dataset()
            observatory = w_dataset.get_id_by_name(planet.pl_facility)
            if observatory is not None:
                confirmed_planets_collection.insert_one(
                    {"pl_hostname": planet.pl_hostname,
                     "pl_letter": planet.pl_letter,
                     "pl_name": planet.pl_name,
                     "pl_discmethod": planet.pl_discmethod,
                     "pl_controvflag": planet.pl_controvflag,
                     "rowupdate": planet.rowupdate,
                     "pl_facility_id": observatory["_id"],
                     "pl_masse": planet.pl_masse,
                     "pl_locale": planet.pl_locale,
                     "ra": planet.ra,
                     "dec": planet.dec,})

        print("All planets are successfully inserted!")

    def get_planets_list(self, request_url):

        planets_list_json = requests.get(request_url).text
        planets_array = json.loads(planets_list_json, object_hook=lambda d: namedtuple('X', d.keys())(*d.values()))

        return planets_array


class Wiki_dataset():

    def generate(self):
        wiki_url = 'https://en.wikipedia.org/wiki/List_of_astronomical_observatories'

        # parsing observatories page on wikipedia
        observatories_arr = self.parse_to_db(wiki_url)

        # adding parsed values to db
        self.fill_db(observatories_arr)

    def fill_db(self, arr):
        client = MongoClient('mongodb://dbuser1:dbuser1dbuser1@localhost:27200/space_exploration')
        observatories_collection = client.space_exploration.observatories
        for observatory in arr:
            observatories_collection.insert_one({"name": observatory[0], "foundation_year": observatory[1], "location": observatory[2]})
        print("All observatories are successfully inserted!")

    def parse_to_db(self, url):

        website_url = requests.get(url).text
        soup = BeautifulSoup(website_url,'lxml')

        list_of_rows = []

        for table in soup.findAll('table', {'class': 'wikitable'})[1:]:
            # remove all extra tags in the HTML Tables
            for div in soup.findAll('span', 'sortkey'):
                div.extract()
            for div in soup.findAll('span', {'style':'display:none'}):
                div.extract()


            #scan through table
            for row in table.findAll('tr')[1:]:
                list_of_cells = []
                for cell in row.findAll('td'):
                    list_of_cells.append(cell.text.split('\n')[0])

                # extracting wrong formatted cells from output
                if (len(list_of_cells) == 3):
                    list_of_rows.append(list_of_cells)

        return list_of_rows

    def get_id_by_name(self, name):
        client = MongoClient('mongodb://dbuser1:dbuser1dbuser1@localhost:27200/space_exploration')
        observatories_collection = client.space_exploration.observatories

        observatories_collection.create_index([('name', pymongo.TEXT)], name='search_index', default_language='english')
        return observatories_collection.find_one({"$text": {"$search": name}})

# uncomment in case you need to fill db

db = Dataset()
db.generate()