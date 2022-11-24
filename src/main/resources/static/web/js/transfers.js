const app = Vue.createApp({
    data(){
        return{
            picked: "",
            texto: "holaaaa",
            url: "http://localhost:8080/api/clients/current",
            cuentas: [],
            cuentasActivas: [],
            cuentaFiltradaActiva: [],
            origen: "",
            destino: "",
            monto: 0,
            descripcion: "",
            balance: "",
        }
    },
    created(){
        this.getdata()
    },
    methods:{
        getdata(){
            axios.get(this.url)
            .then(data => {
                this.cuentas = data.data.accounts.sort((a,b) => a.id - b.id)
                this.cuentasActivas = data.data.accounts.filter(cuenta => cuenta.active == true)
                console.log(this.cuentas)
                console.log(this.picked)
                console.log(this.cuentaFiltrada)
            })
        },
        realizarTransaccion(){
            Swal.fire({
                title: '¿Estás seguro?',
                text: "¡Este proceso no se podrá revertir!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#7b4aee',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, enviar dinero',
                cancelButtonText: 'Cancelar',
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/transactions',`amount=${this.monto}&description=${this.descripcion}&accountO=${this.origen}&accountD=${this.destino}`).catch(function (error){
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                        })
                    })
                    Swal.fire({
                        title: 'Realizando transacción',
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
                            title: 'Transaccion completa',
                            text: 'Su dinero ha sido enviado',
                            icon: 'success',
                            confirmButtonColor: '#7b4aee'
                        }).then(response => window.location.href = "/web/accounts.html")
                      })
                }
              })

        },
        calcularBalance(array){
            balance = this.cuentas.filter(cuenta => cuenta.number == array)
            balancefiltrado = balance.map(a => a.balance)
            balanceCalculado = balancefiltrado
            balanceModificado = this.modificarSaldo(balanceCalculado)
            return balanceModificado
        },
        modificarSaldo(saldo){
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        LogOut(){
            axios.post('/api/logout').then(response=> window.location.href = "/web/index.html")
        },
    },
    computed:{
        filtrarArrayCuentas(){
            if(this.origen != ""){
                cuentaFiltrada = this.cuentas.filter(cuenta => cuenta.number != this.origen)
                this.cuentaFiltradaActiva = cuentaFiltrada.filter(cuenta => cuenta.active == true)
            }
        },
    }
})
app.mount("#app")