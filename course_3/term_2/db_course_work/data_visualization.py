import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from matplotlib.axes import Axes

from db_operations import Database
import numpy as np

class Visualize():
    sns.set(style="ticks", color_codes=True)

    def draw_barplot(self, x, y, title, xlabel, ylabel):

        plt.subplots(figsize=(20,8), num=title)
        sns.barplot(x = x, y=y, palette="pastel", edgecolor=".6")

        plt.subplots_adjust(left=0.2)

        plt.title(title, fontsize=15, y=1.05, weight='bold')
        plt.ylabel(ylabel, weight='bold')
        plt.xlabel(xlabel, weight='bold')

        plt.show()

    def draw_donut_chart(self, data, title):
        # Pie chart
        labels = data['x']
        sizes = data['y']

        fig1, ax1 = plt.subplots(num=title)
        ax1.pie(sizes, labels=labels, autopct='%1.1f%%',
                shadow=True, startangle=90)
        # Equal aspect ratio ensures that pie is drawn as a circle
        ax1.axis('equal')
        plt.tight_layout()
        plt.show()

    def draw_two_density_plots(self, space_arr, ground_arr, title):

        plt.subplots(num=title)

        plt.plot('x1', 'y1', data={'x1': space_arr['years'], "y1": space_arr['counts']},
                 marker='o', markerfacecolor='blue', color='skyblue', linewidth=2, label="Space")
        plt.plot('x2', 'y2', data={'x2': ground_arr['years'], "y2": ground_arr['counts']},
                 marker='o', markerfacecolor='brown', color='lightcoral', linewidth=2, label="Ground")
        plt.legend()

        plt.title('Planets discovery statistics by discovery locale', fontsize=10)
        plt.yticks(fontsize=10)
        plt.ylabel('Quantity of discovered planets', fontsize=10)
        plt.xlabel('Years')
        plt.show()

    def draw_percent_state_barplot(self, normal, controversial, title):
        plt.subplots(figsize=(20, 9.5), num=title)

        r = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        # plot
        barWidth = 0.85
        # Create green Bars
        plt.bar(r, normal["accuracy"], color='skyblue', edgecolor='white', width=barWidth, label="The planets were confirmed")
        # Create orange Bars
        plt.bar(r, controversial["accuracy"], bottom=normal["accuracy"], color='lightcoral', edgecolor='white', width=barWidth, label="The confirmation status of planets has been questioned")

        # Custom x axis
        plt.xticks(r, normal["name"])
        plt.xlabel("Discovery method")

        # Add a legend
        plt.legend(loc='upper left', bbox_to_anchor=(0.65, 0.2), ncol=1)

        plt.xticks(rotation=90)
        plt.subplots_adjust(bottom=0.3)
        # Show graphic
        plt.show()


v = Visualize()
db = Database()

# uncomment to draw a barplot with quantity
# of discovered planets by locations
# vars = db.getTopLocations(15)
# v.draw_barplot(vars["y"], vars["x"], "Top countries by quantity of discovered planets")

# uncomment to draw a barplot with quantity
# of discovered planets by facilities
# vars = db.getTopFacilities(15)
# v.draw_barplot(vars["y"], vars["x"], "Top facilities by quantity of discovered planets")

# uncomment to draw a barplot with percentsge
# of discovered planets by locations among all discovered planets
# vars = db.getTopLocationsByPercentage(15)
# v.draw_barplot(vars["y"], vars["x"], "Top facilities by percentage of discovered planets")

#vars = db.getTopFacilities(3)

# v.draw_percent_state_barplot()
