const app = Vue.createApp({
    data(){
        return{
            url:"http://localhost:8080/api/clients/current",
            cuentaMelba: [],
            tarjetasCredito: [],
            tarjetasDebito: [],
            tarjetasCreditoActivas: [],
            tarjetasDebitoActivas: [],
            tarjetas: [],
            tarjetasActivas: [],
            fechaActual: "",
            modal: false,
            numeroTarjeta: "",
        }
    },
    created(){
        this.getdata()
        this.fechaActual = `${new Date().getFullYear()}-${new Date().getMonth() + 1}` ;
        console.log(this.fechaActual)

    },
    methods:{
        getdata(){
            axios.get(this.url)
            .then(data =>{
                this.cuentaMelba = data.data
                this.tarjetas = data.data.cards
                this.tarjetasCredito = data.data.cards.filter(element => element.type == "CREDIT").sort((a,b) => a.id - b.id)
                this.tarjetasDebito = data.data.cards.filter(element => element.type == "DEBIT").sort((a,b) => a.id - b.id)
                this.tarjetasCreditoActivas = this.tarjetasCredito.filter(card => card.active == true)
                this.tarjetasDebitoActivas = this.tarjetasDebito.filter(card => card.active == true)
                this.tarjetasActivas = this.tarjetas.filter(cards => cards.active == true)
                console.log(data.data.cards)
            })
        },
        modificarFecha(fecha){
            let fechaNueva = fecha.split("T")
            let fechaNueva2 = fechaNueva[0].split("-")
            return `${fechaNueva2[1]}-${fechaNueva2[0]}`
        },
        LogOut(){
            axios.post('/api/logout').then(response=> window.location.href = "/web/index.html")
        },
        createCards(){
            window.location.href = "/web/create-cards.html"
        },
        eliminarTarjeta(){
            axios.delete('/api/clients/current/cards', `number=4845-2152-6594-2561`).then(response => window.location.reload())
        },
        eliminarTarjetas(){
            Swal.fire({
                title: '¡Cuidado!',
                text: "¿Estás seguro de eliminar esta tarjeta?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#7b4aee',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar tarjeta',
                cancelButtonText: 'Cancelar',
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch('/api/clients/current/delete/cards', `number=${this.numeroTarjeta}`).catch(function (error){
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                        })
                    })
                  Swal.fire({
                    title: 'Tarjeta eliminada',
                    icon: 'error',
                    confirmButtonColor: '#7b4aee'
                  }).then(response=> this.getdata())
                }
              })



/*             axios.patch('/api/clients/current/delete/cards', `number=${this.numeroTarjeta}`).then(response => window.location.reload()) */
        },
    },
})
app.mount("#app")