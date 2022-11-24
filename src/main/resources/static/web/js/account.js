const app = Vue.createApp({
    data(){
        return{
            urlaccount: `/api/accounts/`,
            cuenta: [],
            transacciones: [],
            id: (new URLSearchParams(location.search)).get("id"),
            modal: false,
            dateFrom: "",
            dateTo: "",
            numeroCuenta: "",
        }
    },
    created(){
        this.getdata()
    },
    methods:{
        getdata(){
            axios.get(this.urlaccount + this.id)
            .then(data => {
                this.cuenta = data.data
                this.transacciones = this.cuenta.transactions.sort((a,b) => a.id - b.id).reverse()
                this.numeroCuenta = this.cuenta.number
                console.log(this.cuenta)
                console.log(this.transacciones)
                console.log(this.numeroCuenta)
            })
        },
        modificarFecha(fecha){
            let fechaNueva = fecha.split("T")
            let fechaNueva2 = fechaNueva[0].split("-")
            return `${fechaNueva2[2]}-${fechaNueva2[1]}-${fechaNueva2[0]}`
        },
        modificarHora(fecha){
            let fechaNueva = fecha.split(".")
            let fechaNueva2 = fechaNueva[0].split("T")
            return `${fechaNueva2[1]}`
        },
        modificarSaldo(saldo){
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        LogOut(){
            axios.post('/api/logout').then(response=> window.location.href = "/web/index.html")
        },
        downloadPDF() {
            axios.post('/api/transactions/pdf',{
            "dateFrom":this.dateFrom,
            "dateTo":this.dateTo,
            "accountNumber":this.numeroCuenta,                
        } 
              
            )
            .then(response => console.log('pdf downloaded!!!'))
            .catch(error => console.log(error))
          },
    }
})
app.mount("#app")