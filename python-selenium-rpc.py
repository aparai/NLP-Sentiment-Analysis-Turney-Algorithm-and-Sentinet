# -*- coding: utf-8 -*-
"""
Created on Mon Aug 18 21:11:32 2014

@author: parai
"""


from SimpleXMLRPCServer import SimpleXMLRPCServer

#from selenium.webdriver.support.ui import WebDriverWait
import selenium

from selenium.webdriver.support import expected_conditions as EC
from selenium import webdriver

import os, time
from pyvirtualdisplay import Display

def get_count(n):
    display = Display(visible=0, size=(800, 600))
    display.start()
    os.system("export DISPLAY=:99")
    wd = webdriver.Firefox() # uses Firefox
    parts = str(n).split("#")[:-1]
    my_result_count = []
    for ind in range(len(parts)):
        g = str(parts[ind]).strip().replace(" ", "+")
        
        wd.quit()
        wd = webdriver.Firefox() # uses Firefox
        wd.get("https://www.google.co.in/?gws_rd=ssl#q=" + g +"+near+excellent")
        time.sleep(2)        
        for elem in wd.find_elements_by_xpath('.//div[@id="resultStats"]'):
            try:
                z = elem.text                
                the_l = z.split(" ")[1].replace(",", "")      
                my_result_count.append(str(the_l))  
                break
            except:
                wd.quit()
                display.stop()
                return str(1)
        
        wd.quit()
        wd = webdriver.Firefox() # uses Firefox     
        wd.get("https://www.google.co.in/?gws_rd=ssl#q=" + g +"+near+poor")
        time.sleep(2)        
        for elem in wd.find_elements_by_xpath('.//div[@id="resultStats"]'):
            try:
                z = elem.text                
                the_l = z.split(" ")[1].replace(",", "")         
                my_result_count.append(str(the_l))  
                break
            except:
                wd.quit()
                display.stop()
                return str(1)
    wd.quit()
    display.stop()
    my_list_final = ",".join(my_result_count)
    return my_list_final 


#----------------------------------------------

"""

This is the selenium server! this is to interact with Google!

"""
print "You are about to start the Python Selenium RPC server to interact with Google!\n"
my_port = int(input("Enter an available port number (e.g. 8001): \t"));
server = SimpleXMLRPCServer(("localhost", my_port))
print "Listening on port #\t", my_port
server.register_function(get_count, "get_count")
server.serve_forever()
