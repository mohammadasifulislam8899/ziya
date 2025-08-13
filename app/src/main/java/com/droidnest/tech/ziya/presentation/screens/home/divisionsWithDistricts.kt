package com.droidnest.tech.ziya.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryEditable
import androidx.compose.ui.text.TextStyle
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import kotlin.collections.get

val divisionsWithDistricts = mapOf(
    "ঢাকা" to listOf(
        "dhaka",
        "faridpur",
        "gazipur",
        "gopalganj",
        "kishoreganj",
        "madaripur",
        "manikganj",
        "munshiganj",
        "narayanganj",
        "narsingdi",
        "rajbari",
        "shariatpur",
        "tangail"
    ),
    "চট্টগ্রাম" to listOf(
        "bandarban",
        "brahmanbaria",
        "chandpur",
        "chittagong",
        "comilla",
        "coxsbazar",
        "feni",
        "lakshmipur",
        "noakhali",
        "rangamati"
    ),
    "রাজশাহী" to listOf(
        "bogra",
        "joypurhat",
        "naogaon",
        "natore",
        "chapainawabganj",
        "pabna",
        "rajshahi",
        "sirajganj"
    ),
    "খুলনা" to listOf(
        "bagerhat",
        "jessore",
        "jhenaidah",
        "khulna",
        "kushtia",
        "magura",
        "meherpur",
        "narail",
        "satkhira"
    ),
    "বরিশাল" to listOf("barguna", "barisal", "bhola", "patuakhali", "pirojpur"),
    "সিলেট" to listOf("habiganj", "moulvibazar", "sunamganj", "sylhet"),
    "রংপুর" to listOf(
        "dinajpur",
        "nilphamari",
        "panchagarh",
        "rangpur",
        "thakurgaon"
    ),
    "ময়মনসিংহ" to listOf("jamalpur", "mymensingh", "netrokona", "sherpur")
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DivisionDistrictBottomSheet(
    onDismiss: () -> Unit,
    onDistrictSelected: (String) -> Unit
) {
    var selectedDivision by remember { mutableStateOf<String?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }

    var divisionExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }

    val districtList = divisionsWithDistricts[selectedDivision] ?: emptyList()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "বিভাগ ও জেলা নির্বাচন করুন",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = NotoSerifBengali
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Division Dropdown
            Text(
                text = "বিভাগ নির্বাচন করুন",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = NotoSerifBengali
            )
            ExposedDropdownMenuBox(
                expanded = divisionExpanded,
                onExpandedChange = { divisionExpanded = !divisionExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDivision ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            "বিভাগ",
                            fontFamily = NotoSerifBengali

                        )
                    },
                    placeholder = {
                        Text(
                            "বিভাগ নির্বাচন করুন",
                            fontFamily = NotoSerifBengali
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Division Icon"
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = divisionExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor(PrimaryEditable, true)
                        .fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    textStyle = TextStyle(
                        fontFamily = NotoSerifBengali
                    )
                )

                ExposedDropdownMenu(
                    expanded = divisionExpanded,
                    onDismissRequest = { divisionExpanded = false }
                ) {
                    divisionsWithDistricts.keys.forEach { division ->
                        DropdownMenuItem(
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                            text = {
                                Text(
                                    division,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (division == selectedDivision) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    fontFamily = NotoSerifBengali

                                )
                            },
                            onClick = {
                                selectedDivision = division
                                selectedDistrict = null
                                divisionExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // District Dropdown
            if (!selectedDivision.isNullOrEmpty()) {
                Text(
                    text = "জেলা নির্বাচন করুন",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = NotoSerifBengali

                )

                ExposedDropdownMenuBox(
                    expanded = districtExpanded,
                    onExpandedChange = { districtExpanded = !districtExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedDistrict ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(
                                "জেলা",
                                fontFamily = NotoSerifBengali
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = NotoSerifBengali
                        ),
                        placeholder = {
                            Text(
                                "জেলা নির্বাচন করুন",
                                fontFamily = NotoSerifBengali
                            )
                        },

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = "District Icon"
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor(PrimaryEditable, true)
                            .fillMaxWidth(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = districtExpanded,
                        onDismissRequest = { districtExpanded = false }
                    ) {
                        districtList.forEach { district ->
                            DropdownMenuItem(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                                text = {
                                    Text(
                                        district.replaceFirstChar { it.uppercaseChar() },
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (district == selectedDistrict) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    selectedDistrict = district
                                    districtExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "বাতিল",
                        fontFamily = NotoSerifBengali
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = {
                        selectedDistrict?.let {
                            onDistrictSelected(it)
                            onDismiss()
                        }
                    },
                    enabled = selectedDistrict != null
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "নির্বাচন করুন",
                        fontFamily = NotoSerifBengali
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
