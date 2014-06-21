GScanTool
=========

Generic Scaner tool for QR and barcodes implementations in websites

Protocol

    The app scans a QR encoded URL
    The app download the content at that URL
    The app json decode the answer and look for parameters:
        name , the title of the project
        url , where to send next
        type , prefered data format: get, post, json, plain. default: undefined
        text textmessage to show the user
    The app scans for any QR- or bar-code
    The app sends the QR to the previus given URL
    If the response is JSON and have a parameter text, it is displayed.
    The app scans for next QR- or bar-code
