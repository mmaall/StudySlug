import re
import mechanize
from bs4 import BeautifulSoup
import urllib2
import datetime
import sys
import json
from sets import Set

classes = []
br = mechanize.Browser()     

class Class:
    def __init__(self):
        self.classTitle = ""

def _sanitizeStatus(regStatus):
    if regStatus.lower() in ["open", "o"]:
        return "O"
    if regStatus.lower() in ["all", "a"]:
        return "all"
    return False

def _sanitizeTerm(term, soup):                                    
    try:                                                           
        term = int(term)
    except ValueError:
        if term[-8:] != " Quarter":
                term = term+" Quarter"
        for termOption in soup.find('select', id='term_dropdown').findAll('option'):
            if termOption.getText() == term:
                term = termOption['value']
                break
    return term

def readClasses():

    br.open('https://pisa.ucsc.edu/class_search/')
    response = br.response()
    soup = BeautifulSoup(br.response().read(),"html5lib")
    
    term = _sanitizeTerm(2188, soup)                                #term has been hardcoded for fall 2018 as 2188
    regStatus = _sanitizeStatus('all')

    br.select_form(name='searchForm')   
    br['binds[:term]'] = [str(term)]                    
    br['binds[:reg_status]'] = [regStatus]
    br.find_control('binds[:subject]').get(nr=1).selected = True
    termString = ""
    for termOption in soup.find('select', id='term_dropdown').findAll('option'):
        if termOption['value'] == term:
            termString = termOption.getText()
            break
    
    response = "next</a>"
    pageCount = 0
    totalClasses = 0
    
    for i in range(70): #Hardcoded number may cause bugs if total number of classes increases by more than 25
        
        response = br.submit().read()
        soup = BeautifulSoup(response,"html5lib")
        
        if "returned no matches." in response:
            break
        
        if pageCount == 0:
            totalClasses = int(soup.select('.hide-print')[0].find_all('b')[2].getText())
        
        container = soup.select('.center-block > .panel-body')[0]
        for row in container.find_all('div', class_="panel"):       # find html headers that contains classes
            collumn = 0                                             # 
            c = Class()
            repeat = False                      
            heading = row.find('div', class_='panel-heading')       # read html header that contains specific class
            c.classTitle = heading.find_all('a')[-1].getText()      #
         
            classes.append(c)
        br.select_form(name='resultsForm') # go to next page 
        br.form.set_all_readonly(False)    #
        br['action'] = "next"              #

                                
    return classes

classes = readClasses()
for i in classes:
        print i.classTitle
   
