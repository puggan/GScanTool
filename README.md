GScanTool
=========

Generic Scaner tool for QR and barcodes implementations in websites

## Protocol

1. The app scans a QR encoded URL
2. The app download the content at that URL
3. The app json decode the answer and look for parameters:
  * name , the title of the project
  * url , where to send next
  * type , prefered data format: get, post, json, plain. default: undefined
  * text textmessage to show the user
4. The app scans for any QR- or bar-code
5. The app sends the QR to the previus given URL
6. If the response is JSON and have a parameter text, it is displayed.
7. The app scans for next QR- or bar-code
