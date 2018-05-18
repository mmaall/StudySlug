# -*- coding: cp1252 -*-
#python class database creator yay!
from __future__ import print_function
#take in string 'SUBJ ### - ## String of Name'
#output 
fullname = 'SUBJ 111 - 22 String of Name'
subject = 'SUBJ'
number = '111'
section = '22'
name = 'String of Name'
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

print ('\n"',end=""),
print (fullname,end=""),
print('" : { \n  "'),
print('"department" : "',end=""),
print (subject,end=""),
print('",\n  "number" : "',end=""),
print (number,end=""),
print('",\n  "section" : "',end=""),
print (section,end=""),
print('",\n  "name" : "',end=""),
print (name,end=""),
print('"\n  "students" : [{\n}]\n},',end="")
