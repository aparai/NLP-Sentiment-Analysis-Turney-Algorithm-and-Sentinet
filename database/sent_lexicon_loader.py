# -*- coding: utf-8 -*-
"""
Created on Mon Aug 18 12:20:42 2014

@author: parai
"""

import MySQLdb as mdb

conn = mdb.connect('localhost', 'your_username', 'your_password', 'your_database_name')
c=conn.cursor()

f = open("1.tff", 'r')
u = f.readlines()



for ind in range(len(u))[:-1]:
    print ind
    my_s = u[ind]
    my_l = my_s.split(" ")
    type_s = (my_l[0].split("=")[1]).strip()
    word_s = (my_l[2].split("=")[1]).strip()
    pos_s = (my_l[3].split("=")[1]).strip()
    polarity = (my_l[5].split("=")[1]).strip()
    c.execute("INSERT INTO sent_lexicon_1 (type, word, pos, polarity) VALUES('"+str(type_s)+"','" + str(word_s) + "','" + str(pos_s) + "','" + str(polarity) + "'" + ")")
    
conn.commit()
    
