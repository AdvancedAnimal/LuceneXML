from lxml import etree
import os

# open file

f = open('/Users/sam/Documents/workspace/derp.xml', 'r+w')

# parse file into xml

p = etree.XMLParser(remove_blank_text=True, load_dtd=True)

x = etree.parse(f, p)

# xpath the list of articles

articles = x.xpath('//article')

article = []

# write article files

file_name = 'Articles-Dump-%s.xml'

file_count = 0

    
for a in articles:
    article.append(a)

    #if time to write file
    if len(article) >= 1000:
        #generate file name
        f_name = file_name % str(file_count)
        file_count += 1
        #open file
        f = open(os.path.abspath(f_name), 'w+')
        #write data
        data = ''.join(map(lambda x: etree.tostring(x, pretty_print=True), articles))
        print 'Writing %s' % f_name
        f.write(data)
        f.close()
        article = []


#write last file
if article > 0:
    #generate file name
    f_name = file_name % str(file_count)
    file_count += 1
    #open file
    f = open(os.path.abspath(f_name), 'w+')
    #write data
    data = ''.join(map(lambda x: etree.tostring(x, pretty_print=True), articles))
    print 'Writing %s' % f_name
    f.write(data)
    f.close()
    article = []





