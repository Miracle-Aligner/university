import tkinter
from db_operations import Database
from data_visualization import Visualize

v = Visualize()
db = Database()

window = tkinter.Tk()
# Code to add widgets will go here...

def show_top_locations():
    side = tkinter.Tk()

    def clicked():
        vars = db.get_top_locations(int(spinbox.get()))
        v.draw_barplot(vars["y"], vars["x"], "Top locations by quantity of discovered planets", "Quantity of discovered planets", "Locations names")

    lbl = tkinter.Label(side, text="Enter number of top locations you want to see displayed on a graph (max == 15): ", wraplength="350", justify="left")
    spinbox = tkinter.Spinbox(side, from_=1, to=15)
    btn = tkinter.Button(side, text="Show", command=clicked)

    btn.grid(column=2, row=0, padx=(10, 10), pady=(100, 0))
    lbl.grid(column=0, row=0, padx=(100, 10), pady=(100, 10))
    spinbox.grid(column=1, row=0, padx=(10, 0), pady=(100, 10))

    side.geometry('800x250')
    side.title("Top locations by quantity of discovered planets")

    side.mainloop()


def show_top_facilities():
    side = tkinter.Tk()

    def clicked():
        vars = db.get_top_facilities(int(spinbox.get()))
        v.draw_barplot(vars["y"], vars["x"], "Top facilities by quantity of discovered planets", "Quantity of discovered planets", "Facilities names")

    lbl = tkinter.Label(side, text="Enter number of top facilities you want to see displayed on a graph (max == 31): ",
                        wraplength="350", justify="left")
    spinbox = tkinter.Spinbox(side, from_=1, to=15)
    btn = tkinter.Button(side, text="Show", command=clicked)

    btn.grid(column=2, row=0, padx=(10, 10), pady=(100, 0))
    lbl.grid(column=0, row=0, padx=(100, 10), pady=(100, 10))
    spinbox.grid(column=1, row=0, padx=(10, 0), pady=(100, 10))

    side.geometry('800x250')
    side.title("Top facilities by quantity of discovered planets")

    side.mainloop()

def show_top_locations_percentage():
    side = tkinter.Tk()

    def clicked():
        vars = db.get_top_locations(int(spinbox.get()))
        v.draw_donut_chart(vars, "Top locations by quantity of discovered planets in percents")

    lbl = tkinter.Label(side, text="Enter number of top locations you want to see displayed on a graph (max == 15): ",
                        wraplength="350", justify="left")
    spinbox = tkinter.Spinbox(side, from_=1, to=15)
    btn = tkinter.Button(side, text="Show", command=clicked)

    btn.grid(column=2, row=0, padx=(10, 10), pady=(100, 0))
    lbl.grid(column=0, row=0, padx=(100, 10), pady=(100, 10))
    spinbox.grid(column=1, row=0, padx=(10, 0), pady=(100, 10))

    side.geometry('800x250')
    side.title("Top locations by quantity of discovered planets in percents")

    side.mainloop()

def show_top_facilities_percentage():
    side = tkinter.Tk()

    def clicked():
        vars = db.get_top_facilities(int(spinbox.get()))
        v.draw_donut_chart(vars, "Top facilities by quantity of discovered planets in percents")

    lbl = tkinter.Label(side, text="Enter number of top facilities you want to see displayed on a graph (max == 31): ",
                        wraplength="350", justify="left")
    spinbox = tkinter.Spinbox(side, from_=1, to=31)
    btn = tkinter.Button(side, text="Show", command=clicked)

    btn.grid(column=2, row=0, padx=(10, 10), pady=(100, 0))
    lbl.grid(column=0, row=0, padx=(100, 10), pady=(100, 10))
    spinbox.grid(column=1, row=0, padx=(10, 0), pady=(100, 10))

    side.geometry('800x250')
    side.title("Top facilities by quantity of discovered planets in percents")

    side.mainloop()

def discovery_statistics_by_years():

    space = db.get_discovery_stat("Space")
    ground = db.get_discovery_stat("Ground")
    v.draw_two_density_plots(space, ground, "Planets discovery statistics by discovery locale")

def accuracy_of_discovering_methods():

    normal = db.get_discovery_accuracy_stat(0)
    controversial = db.get_discovery_accuracy_stat(1)
    v.draw_percent_state_barplot(normal, controversial, "Accuracy of discovery methods")

def predict_planets_discovery():
    side = tkinter.Tk()

    text = "Next year discovered planets quantity prediction: " + str(db.predict_f_discovered_planets_quantity()[0])
    lbl = tkinter.Label(side, text=text, wraplength="350", justify="center")

    side.geometry('350x200')
    side.title("Planets discovery prediction")

    lbl.place(relx=0.5, rely=0.5, anchor="center")

    side.mainloop()

window.geometry('700x500')
window.title("Space Exploration")

button1 = tkinter.Button(window, text ="Top locations", command = show_top_locations)
button2 = tkinter.Button(window, text ="Top facilities", command = show_top_facilities)
button3 = tkinter.Button(window, text ="Top locations percentage", command = show_top_locations_percentage)
button4 = tkinter.Button(window, text ="Top facilities percentage", command = show_top_facilities_percentage)
button5 = tkinter.Button(window, text ="Discovery statistics by years", command = discovery_statistics_by_years)
button6 = tkinter.Button(window, text ="Accuracy of discovering methods", command = accuracy_of_discovering_methods)
button7 = tkinter.Button(window, text ="Planets discovery prediction", command = predict_planets_discovery)

button1.place(relx=0.5, rely=0.2, anchor="center")
button2.place(relx=0.5, rely=0.3, anchor="center")
button3.place(relx=0.5, rely=0.4, anchor="center")
button4.place(relx=0.5, rely=0.5, anchor="center")
button5.place(relx=0.5, rely=0.6, anchor="center")
button6.place(relx=0.5, rely=0.7, anchor="center")
button7.place(relx=0.5, rely=0.8, anchor="center")


window.mainloop()