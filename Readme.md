##Elasticsearch
```
sudo sysctl -w vm.max_map_count=262144
```
Start by adding an index
```
curl -XPUT "http://localhost:9200/music/"
```
then add a song
```
curl -XPUT "http://localhost:9200/music/songs/1" -d '
{ "name": "Deck the Halls", "year": 1885, "lyrics": "Fa la la la la" }'
```
look up that song
```
curl -XGET "http://localhost:9200/music/songs/1"
```
add a lyric using provided fil
```
curl -XPUT "http://localhost:9200/music/lyrics/2" -d @walking.json
```
add another lyric
```
curl -XPUT "http://localhost:9200/music/lyrics/1" -d @caseyjones.json
```

##MQTT
..