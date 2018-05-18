# -*- coding: cp1252 -*-
from __future__ import print_function


with open('c:/Users/Kian/classlist.txt') as f:
    content = f.readlines()
# you may also want to remove whitespace characters like `\n` at the end of each line
    for i in content:
        fullname = i
        j = i.split(' ',3)
    
        subject = j[0]
        number = j[1]
        #i[2] = '-'
        section = j[3][0:2]
        name = j[3][3:]
        print ('\n"',end=""),
        print (fullname[0:len(fullname)-1],end='"'),
        print(' : { \n  '),
        print('"department" : "',end=""),
        print (subject,end=""),
        print('",\n  "number" : "',end=""),
        print (number,end=""),
        print('",\n  "section" : "',end=""),
        print (section,end=""),
        print('",\n  "name" : "',end=""),
        print (name[0:len(name)-1],end=""),
        print('"\n  "students" : [{\n}]\n},',end="")
        
            

#python class database creator yay!

#take in string 'SUBJ ### - ## String of Name'
#output 

#{
#    " SUBJ ### - ## String of Name
#
#
#" : {
#      "department" : "SUBJ
#
#",
#      "number" : "###
#
#",
#      "section" : "##
#
#
#",
#	  "name" : "String of Name
#
#"
#      "students" : [{
#      }]
#    },


