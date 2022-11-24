const app = Vue.createApp({
    data(){
        return{
            urlLoans:"http://localhost:8080/api/loans",
            urlClient: "http://localhost:8080/api/clients/current",
            texto: "dsa",
            prestamos: [],
            idPrestamo: 16,
            cuotasPrestamo: 12,
            montoPrestamo: 0,
            cuentaDestino: "",
            cuentas: [],
            cuotasHipotecario: [],
            cuotasPersonal: [],
            cuotasAutomotriz: [],
            cuentasActivas: [],
        }
    },
    created(){
        this.getLoans()
        this.getClient()

    },
    methods:{
        getLoans(){
            axios.get(this.urlLoans)
            .then(data => {
                this.prestamos = data.data
                this.cuotasHipotecario = data.data[0].payments
                this.cuotasPersonal = data.data[1].payments
                this.cuotasAutomotriz = data.data[2].payments
                console.log(data.data)
            })
        },
        getClient(){
            axios.get(this.urlClient)
            .then(data => {
                this.cuentas = data.data.accounts
                this.cuentasActivas = data.data.accounts.filter(cuenta => cuenta.active == true)
                console.log(data.data)
            })
        },
        solicitarPrestamo(){
            Swal.fire({
                title: '¿Estás seguro de solicitar el prestamo?',
                text: "¡Este proceso no se podrá revertir!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#7b4aee',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, solicitar prestamo',
                cancelButtonText: 'Cancelar',
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans',{ 
                        "id": this.idPrestamo, 
                        "amount": this.montoPrestamo, 
                        "payments": this.cuotasPrestamo, 
                        "accountDestiny": this.cuentaDestino
                    }).catch(function (error){
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                        })
                    })
                    Swal.fire({
                        title: 'Solicitando prestamo',
                        html: 'Este proceso puede demorar unos segundos',
                        timer: 2500,
                        timerProgressBar: true,
                        didOpen: () => {
                          Swal.showLoading()
                          const b = Swal.getHtmlContainer().querySelector('b')
                          timerInterval = setInterval(() => {
                            b.textContent = Swal.getTimerLeft()
                          }, 100)
                        },
                        willClose: () => {
                          clearInterval(timerInterval)
                        }
                      }).then((result) => {
                        /* Read more about handling dismissals below */
                        if (result.dismiss === Swal.DismissReason.timer) {
                          console.log('I was closed by the timer')
                        }
                        Swal.fire({
                            title: 'Prestamo aprobado',
                            text: 'Su dinero ha sido acreditado',
                            icon: 'success',
                            confirmButtonColor: '#7b4aee'
                        }).then(response => window.location.href = "/web/accounts.html")
                      })
                }
              })

        },

        LogOut(){
            axios.post('/api/logout').then(response=> window.location.href = "/web/index.html")
        },
        saldoMasInteres(){
            if(this.idPrestamo != ""){
                if(this.idPrestamo == 16){
                    saldoInteres = this.montoPrestamo * 1.2
                }
                if(this.idPrestamo == 17){
                    saldoInteres = this.montoPrestamo * 1.1
                }
                if(this.idPrestamo == 18){
                    saldoInteres = this.montoPrestamo * 1.15
                }
                return saldoInteres
            }
        },
        cuotasConInteres(){
            if(this.idPrestamo != "" && this.cuotasPrestamo != ""){
                if(this.idPrestamo == 16){
                    saldoInteres = this.montoPrestamo * 1.2
                    cuotas = saldoInteres / this.cuotasPrestamo
                }
                if(this.idPrestamo == 17){
                    saldoInteres = this.montoPrestamo * 1.1
                    cuotas = saldoInteres / this.cuotasPrestamo
                }
                if(this.idPrestamo == 18){
                    saldoInteres = this.montoPrestamo * 1.15
                    cuotas = saldoInteres / this.cuotasPrestamo
                }
                return cuotas
            }
        },
        modificarSaldo(saldo){
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
    },
    computed:{

    }
})
app.mount('#app')