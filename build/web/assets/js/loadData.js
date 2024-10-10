/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


async function LoadData() {

    const response = await fetch("LoadData");

    if (response.ok) {
        const json = await response.json();

        console.log(json);


        const AuthorlList = json.AuthorlList;
        const ConditionList = json.ConditionList;
        const TypeList = json.TypeList;

        loadSelect("author", AuthorlList);
        loadSelect("condition", ConditionList);
        loadSelect("type", TypeList);


    } else {
        console.log("Something Error");
    }
}

function loadSelect(selectTagId, list) {
    const selectTag = document.getElementById(selectTagId);
    list.forEach(data => {
        let optionTag = document.createElement("option");
        optionTag.value = data.id;
        optionTag.innerHTML = data.name;
        selectTag.appendChild(optionTag);
    });
}

