import pandas as pd
import requests;
import lxml
import html5lib

from bs4 import BeautifulSoup

website_url = requests.get('https://en.wikipedia.org/wiki/List_of_astronomical_observatories').text

soup = BeautifulSoup(website_url,'lxml')
observatories_tables = soup.findAll('table',{'class':'wikitable'})

# flag to prevent parsing of *legend* table
flag = 0;

for table in soup.findAll('table',{'class':'wikitable'}):
    print(table.tbody)
    tr = [tr for tr in table.tbody]

    if flag >= 1:
        td = [td for td in tr]

        #if td.attrs['bgcolor'] == "#99ccff":
            #print("CUNT")

        #for td in td:
            #print(td)

    else:
        flag = 1
