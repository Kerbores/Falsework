//map 组件
<template>
  <div class="full">
    <div id="search" class="row">
      <div class="col-xs-12 col-md-12 col-sm-12">
        <div class="input-group">
          <input class="form-control" type="text" placeholder="请输入地址" id="autoComplete" name="autoComplete">
          <span class="input-group-addon">
            <i class="glyphicon glyphicon-search"></i>
          </span>
        </div>
      </div>
      <div class="col-xs-4"></div>
    </div>
    <div id="allmap"></div>
  </div>
</template>
<style>
html,
body,
#app,
#allmap,
.full {
  width: 100%;
  height: 100%;
  margin: 0;
  font-family: "微软雅黑";
}

.row {
  margin-right: 0;
  margin-left: 0
}

.input-group {
  margin-right: -15px;
  margin-left: -15px
}
</style>
<script>
import {
  MP
} from './map'
import bootstrap from '../../../node_modules/bootstrap/dist/css/bootstrap.min.css'
export default {
  props: {
    ak: {
      type: String,
      required: true
    },
    center: {
      type: Object,
      required: false
    },
  },
  methods: {
    notify(rs) {
      this.$emit('notify', rs)
    }
  },
  mounted() {
    const self = this;
    this.$nextTick(function () {
      MP(this.ak).then(BMap => {
        var map = new BMap.Map("allmap"); // 创建Map实例
        var point = new BMap.Point(this.center ? this.center.lng : 0, this.center ? this.center.lat : 0); // 创建点坐标
        map.centerAndZoom(point, 15);
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function (r) {
          if (this.getStatus() == BMAP_STATUS_SUCCESS) {
            new BMap.Geocoder().getLocation(r.point, function (rs) {
              var marker = new BMap.Marker(rs.point);
              map.clearOverlays();
              map.centerAndZoom(rs.point, 15);
              map.addOverlay(marker); // 将标注添加到地图中
              self.notify(rs);
            });
          }
        }, { enableHighAccuracy: true })
        map.addControl(new BMap.MapTypeControl()); //添加地图类型控件
        map.enableScrollWheelZoom(true);
        //点击监听
        map.addEventListener("click", function (e) {
          var pt = e.point;
          new BMap.Geocoder().getLocation(pt, function (rs) {
            var marker = new BMap.Marker(rs.point);
            map.clearOverlays();
            map.addOverlay(marker); // 将标注添加到地图中
            self.notify(rs);
          });
        });
        //自动完成
        var ac = new BMap.Autocomplete({ //建立一个自动完成的对象
          "input": "autoComplete",
          "location": map
        });
        //鼠标放在下拉列表上的事件
        ac.addEventListener("onhighlight", function (e) {
          var str = "";
          var _value = e.fromitem.value;
          var value = "";
          if (e.fromitem.index > -1) {
            value = _value.province + _value.city + _value.district + _value.street + _value.business;
          }
          str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
          value = "";
          if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province + _value.city + _value.district + _value.street + _value.business;
          }
          str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
          // $("#searchResultPanel").html(str);
        });
        //
        ac.addEventListener("onconfirm", function (e) { //鼠标点击下拉列表后的事件
          var _value = e.item.value;
          var myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
          var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: function () {
              var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
              new BMap.Geocoder().getLocation(pp, function (rs) {
                var marker = new BMap.Marker(rs.point);
                map.centerAndZoom(rs.point, 18);
                map.clearOverlays();
                map.addOverlay(marker); // 将标注添加到地图中
                self.notify(rs);
              });
            }
          });
          local.search(myValue);
        });
      })
    })
  }
}
</script>
