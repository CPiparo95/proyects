var app = new Vue({
    el: '#app',
    data: {
        members : [],
        tenPercent : 0,
        atGlance: {
            num_dem: 0,
            num_rep: 0,
            num_inds: 0,
            total: 0,
            votes_dem_party: 0,
            votes_rep_party: 0,
            votes_inds_party: 0,
            votes_dem_attendance: 0,
            votes_rep_attendance: 0,
            votes_inds_attendance: 0,
        },
        members_sort : [],
        members_reverse_sort : []
    },
    created: () => {
        let url = document.getElementById("senate")
        ? "https://api.propublica.org/congress/v1/113/senate/members.json" 
        : "https://api.propublica.org/congress/v1/113/house/members.json"

        let key = "hgAkxR7fIwUj9BOTbv51ZCyXGlVXvV134mqIrctH"

        fetch(url,{
            method: 'GET',
            headers: {
                'X-API-Key': key
            }
        }).then(function(response){
            if(response.ok){
                return response.json()
            }else{
                throw new Error()
            }
        }).then(function(json){
            app.members = json.results[0].members
            app.calculate()
        }).catch(function(error){
            console.log(error)
        })     
},
    methods:{
        redondeo(numero) {
            return Math.round(numero);
        },
        calculate(){
            this.members.forEach(e => {
                //llenado de members con los datos a utilizar en la tabla
                this.name =
                (e.first_name + " " + (e.middle_name || "") + " " + e.last_name);
                this.url = e.url;
                this.missed_votes = e.missed_votes;
                this.party_votes_percent = e.votes_with_party_pct;
                this.num_party_votes = (((e.total_votes - e.missed_votes) * e.votes_with_party_pct) / 100);
                this.percent_missed_votes = ((e.missed_votes * 100) / e.total_votes);
                this.members_sort.push({
                    name: this.name,
                    party_votes_percent: this.redondeo(this.party_votes_percent),
                    url: this.url,
                    num_party_votes: this.redondeo(this.num_party_votes),
                    missed_votes : this.missed_votes,
                    percent_missed_votes : this.redondeo(this.percent_missed_votes)
                });
                //llenado de datos para glance
                switch(e.party){
                    case "D":
                        this.atGlance.num_dem ++;
                        this.atGlance.votes_dem_party += e.votes_with_party_pct;
                        this.atGlance.votes_dem_attendance += (e.total_votes - e.missed_votes);
                        break
                    case "R": 
                        this.atGlance.num_rep ++;
                        this.atGlance.votes_rep_party += e.votes_with_party_pct;
                        this.atGlance.votes_rep_attendance += (e.total_votes - e.missed_votes);
                        break
                    case "I": 
                        this.atGlance.num_inds ++;
                        this.atGlance.votes_inds_party += e.votes_with_party_pct;
                        this.atGlance.votes_inds_attendance += (e.total_votes - e.missed_votes);
                        break
                }
            });
            this.tenPercent = this.redondeo((this.members.length * 10) / 100);
            this.atGlance.votes_dem_party = this.atGlance.votes_dem_party / this.atGlance.num_dem;
            this.atGlance.votes_dem_attendance = this.atGlance.votes_dem_attendance / this.atGlance.num_dem;
            this.atGlance.votes_rep_party = this.atGlance.votes_rep_party / this.atGlance.num_rep;
            this.atGlance.votes_rep_attendance = this.atGlance.votes_rep_attendance / this.atGlance.num_rep;
            this.atGlance.votes_inds_party = this.atGlance.votes_inds_party / this.atGlance.num_inds;
            this.atGlance.votes_inds_attendance = this.atGlance.votes_inds_attendance / this.atGlance.num_inds;
            this.atGlance.total = this.members.length;

            if(document.getElementById("loyalty")){
                this.members_sort.sort(function (a, b) {
                    if (a.party_votes_percent < b.party_votes_percent) {
                        return 1;
                    }
                    if (a.party_votes_percent > b.party_votes_percent) {
                        return -1;
                    }
                    // a must be equal to b
                    return 0;
                });
            }else{
                this.members_sort.sort(function (a, b) {
                    if (a.percent_missed_votes < b.percent_missed_votes) {
                        return 1;
                    }
                    if (a.percent_missed_votes > b.percent_missed_votes) {
                        return -1;
                    }
                    // a must be equal to b
                    return 0;
                });
            }
            this.members_reverse_sort = this.members_sort.slice();
            this.members_reverse_sort.reverse();
            this.members_sort = this.members_sort.splice(0,this.tenPercent);
            this.members_reverse_sort = this.members_reverse_sort.splice(0,this.tenPercent);
        }
    }
});