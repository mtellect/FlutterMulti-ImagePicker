import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_multiple_image_picker/flutter_multiple_image_picker.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  BuildContext context;
  String _platformMessage = 'No Error';
  List images;
  int maxImageNo = 10;
  bool selectSingleImage = false;

  @override
  initState() {
    super.initState();
  }

  initMultiPickUp() async {
    setState(() {
      images = null;
      _platformMessage = 'No Error';
    });
    List resultList;
    String error;
    try {
      resultList = await FlutterMultipleImagePicker.pickMultiImages(
          maxImageNo, selectSingleImage);
    } on PlatformException catch (e) {
      error = e.message;
    }

    if (!mounted) return;

    setState(() {
      images = resultList;
      if (error == null) _platformMessage = 'No Error Dectected';
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Multi-image picker plugin'),
        ),
        body: new Container(
          padding: const EdgeInsets.all(8.0),
          child: new Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              images == null
                  ? new Container(
                      height: 300.0,
                      width: 400.0,
                      child: new Icon(
                        Icons.image,
                        size: 250.0,
                        color: Theme.of(context).primaryColor,
                      ),
                    )
                  : new SizedBox(
                      height: 300.0,
                      width: 400.0,
                      child: new ListView.builder(
                        scrollDirection: Axis.horizontal,
                        itemBuilder: (BuildContext context, int index) =>
                            new Padding(
                              padding: const EdgeInsets.all(5.0),
                              child: new Image.file(
                                new File(images[index].toString()),
                              ),
                            ),
                        itemCount: images.length,
                      ),
                    ),
              new Padding(
                padding: const EdgeInsets.all(8.0),
                child: new Text('Error Dectected: $_platformMessage'),
              ),
              new RaisedButton.icon(
                  onPressed: initMultiPickUp,
                  icon: new Icon(Icons.image),
                  label: new Text("Pick-Up Images")),
            ],
          ),
        ),
      ),
    );
  }
}
