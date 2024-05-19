package com.example.weather

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.example.weather.api.NetworkResponse
import com.example.weather.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel){
    var city by remember {
        mutableStateOf(value = "")
    }
    val weatherResult = viewModel.weatherResult.observeAsState()

    val keyBoardController= LocalSoftwareKeyboardController.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly){
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {city=it},
                label = {Text(text="Search for any location")}
            )
            IconButton(onClick = {
                viewModel.getCity(city)
                keyBoardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search button")
            }
        }
        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text=result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data =result.data)
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data:WeatherModel){
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription ="Location icon", modifier = Modifier.size(30.dp) )
            Text(text= "${data.location.name},", fontSize = 20.sp)
            Spacer(modifier = Modifier.width((10.dp)))
            Text(text=data.location.country, fontSize = 20.sp,color= Color.Magenta)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c} ° c",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            modifier = Modifier.size((170.dp)),
            model="https:${data.current.condition.icon}".replace("64x64","128x128"), contentDescription="image"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 25.sp,
            color = Color.Green,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column (modifier = Modifier.fillMaxWidth().background(color = Color.Blue)){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    WeatherValues(key = "Humidity", value = data.current.humidity)
                    WeatherValues(key = "Wind Speed - KPH", value = data.current.wind_kph)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    WeatherValues(key = "Uv Radiation", value = data.current.uv)
                    WeatherValues(key = "Feel like ° c", value = data.current.feelslike_c)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    WeatherValues(key = "Cloud Cover %", value = data.current.cloud)
                    WeatherValues(key = "Precipitation in MM", value = data.current.precip_mm)
                }
            }
        }
    }
}
@Composable
fun WeatherValues(key:String,value:String){
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Text(text=value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key,fontSize = 20.sp)
    }
}